package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.domain.feed.dto.FeedRequestDto;
import com.shineidle.tripf.domain.feed.dto.FeedResponseDto;
import com.shineidle.tripf.domain.feed.entity.Activity;
import com.shineidle.tripf.domain.feed.entity.Days;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.feed.repository.ActivityRepository;
import com.shineidle.tripf.domain.feed.repository.DaysRepository;
import com.shineidle.tripf.domain.feed.repository.FeedRepository;
import com.shineidle.tripf.domain.feed.service.FeedServiceImpl;
import com.shineidle.tripf.domain.feed.service.RedisFeedService;
import com.shineidle.tripf.domain.follow.service.FollowService;
import com.shineidle.tripf.domain.geo.service.GeoService;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.service.UserService;
import com.shineidle.tripf.domain.user.type.UserAuth;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.global.common.util.provider.JwtProvider;
import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceImplTest {

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private DaysRepository daysRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private FollowService followService;

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private GeoService geoService;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private RedisFeedService redisFeedService;

    private User loginUser;
    private Feed feed;

    @BeforeEach
    void setUp() {
        loginUser = new User("test@example.com", "password123", "testUser", UserAuth.NORMAL, "123 Street, City");
        ReflectionTestUtils.setField(loginUser, "id", 1L);

        feed = new Feed(loginUser, "Korea", "Seoul", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "Test Title", "Test Content", 10000L, "#tag");
        ReflectionTestUtils.setField(feed, "id", 1L);

        Days days = new Days();
        ReflectionTestUtils.setField(days, "id", 1L);
        Activity activity = new Activity();

        ReflectionTestUtils.setField(activity, "id", 1L);
        loginUser = new User("test@example.com", "password123", "testUser", UserAuth.NORMAL, "123 Street, City");
        ReflectionTestUtils.setField(loginUser, "id", 1L);

        UserDetailsImpl userDetails = new UserDetailsImpl(loginUser);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createFeedSuccessTest() throws InterruptedException {
        // Given
        FeedRequestDto requestDto = new FeedRequestDto(
                "Seoul",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                "New Title",
                "New Content",
                5000L,
                "#travel",
                List.of()
        );

        UserDetailsImpl userDetails = new UserDetailsImpl(loginUser);
        Authentication authMock = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        when(jwtProvider.getAuthentication(anyString())).thenReturn(authMock);

        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(10L, 30L, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        when(feedRepository.save(any(Feed.class))).thenAnswer(invocation -> {
            Feed savedFeed = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedFeed, "id", 1L);
            return savedFeed;
        });

        when(feedRepository.findById(anyLong())).thenAnswer(invocation -> {
            Feed savedFeed = new Feed(
                    loginUser,
                    "Korea",
                    "Seoul",
                    requestDto.getStartedAt(),
                    requestDto.getEndedAt(),
                    requestDto.getTitle(),
                    requestDto.getContent(),
                    requestDto.getCost(),
                    requestDto.getTag()
            );
            ReflectionTestUtils.setField(savedFeed, "id", 1L);
            return Optional.of(savedFeed);
        });

        when(redisFeedService.getFeed(anyLong())).thenReturn(null);

        // When
        FeedResponseDto response = feedService.createFeed(requestDto, "token");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(response.getContent()).isEqualTo(requestDto.getContent());
        assertThat(response.getCost()).isEqualTo(requestDto.getCost());
        assertThat(response.getTag()).isEqualTo(requestDto.getTag());
        assertThat(response.getCity()).isEqualTo(requestDto.getCity());
        assertThat(response.getStartedAt()).isEqualTo(requestDto.getStartedAt());
        assertThat(response.getEndedAt()).isEqualTo(requestDto.getEndedAt());
        verify(feedRepository, times(1)).save(any(Feed.class));
    }


    @Test
    void findFeedSuccessTest() {
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(redisFeedService.getFeed(anyLong())).thenReturn(null);

        // When
        FeedResponseDto response = feedService.findFeed(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCity()).isEqualTo("Seoul");
    }

    @Test
    void findFeedNotFoundTest() {
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.empty());

        when(redisFeedService.getFeed(anyLong())).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> feedService.findFeed(1L))
                .isInstanceOf(GlobalException.class)
                .hasMessage(FeedErrorCode.FEED_NOT_FOUND.getMessage());
    }
}
