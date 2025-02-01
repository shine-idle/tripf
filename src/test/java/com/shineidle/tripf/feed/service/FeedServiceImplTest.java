package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.feed.dto.*;
import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.repository.FeedRepository;
import com.shineidle.tripf.feed.repository.DaysRepository;
import com.shineidle.tripf.feed.repository.ActivityRepository;
import com.shineidle.tripf.follow.service.FollowService;
import com.shineidle.tripf.geo.service.GeoService;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.service.UserService;
import com.shineidle.tripf.user.type.UserAuth;
import com.shineidle.tripf.config.auth.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    void updateFeedSuccessTest() {
        // Given
        FeedRequestDto requestDto = new FeedRequestDto("Seoul", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "Updated Title", "Updated Content", 5000L, "#travel", List.of());

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(geoService.getCountryByCity(anyString())).thenReturn("Korea");

        // When
        FeedResponseDto response = feedService.updateFeed(1L, requestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Updated Title");
        verify(feedRepository, times(1)).save(any(Feed.class));
    }

    @Test
    void updateFeedFeedNotFoundTest() {
        // Given
        FeedRequestDto requestDto = new FeedRequestDto("Seoul", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "New Title", "New Content", 5000L, "#travel", List.of());

        when(feedRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedService.updateFeed(1L, requestDto))
                .isInstanceOf(GlobalException.class)
                .hasMessage(FeedErrorCode.FEED_NOT_FOUND.getMessage());
    }

    @Test
    void findFeedSuccessTest() {
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(daysRepository.findByFeed(feed)).thenReturn(List.of());

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

        // When & Then
        assertThatThrownBy(() -> feedService.findFeed(1L))
                .isInstanceOf(GlobalException.class)
                .hasMessage(FeedErrorCode.FEED_NOT_FOUND.getMessage());
    }

    @Test
    void deleteFeedSuccessTest() {
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));

        // When
        feedService.deleteFeed(1L);

        // Then
        verify(feedRepository, times(1)).save(any(Feed.class));
    }

    @Test
    void deleteFeedFeedNotFoundTest() {
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedService.deleteFeed(1L))
                .isInstanceOf(GlobalException.class)
                .hasMessage(FeedErrorCode.FEED_NOT_FOUND.getMessage());
    }

    @Test
    void findMyFeedsSuccessTest() {
        Pageable pageable = PageRequest.of(0, 10);
        when(feedRepository.findByUserIdAndDeletedAtIsNull(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<MyFeedResponseDto> response = feedService.findMyFeeds(pageable);

        assertThat(response).isNotNull();
        verify(feedRepository, times(1)).findByUserIdAndDeletedAtIsNull(anyLong(), any(Pageable.class));
    }

    @Test
    void findRegionSuccessTest() {
        when(feedRepository.findByCountryAndDeletedAtIsNull(anyString()))
                .thenReturn(List.of(feed));

        List<RegionResponseDto> response = feedService.findRegion("Korea");

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
        verify(feedRepository, times(1)).findByCountryAndDeletedAtIsNull(anyString());
    }

    @Test
    void findHomeDataSuccessTest() {
        when(feedRepository.findByCountry(anyString())).thenReturn(List.of(feed));
        when(feedRepository.findByCountryNot(anyString())).thenReturn(List.of(feed));
        when(followService.findFollowers()).thenReturn(List.of());
        when(followService.findFollowings()).thenReturn(List.of());

        HomeResponseDto response = feedService.findHomeData();

        assertThat(response).isNotNull();
        verify(feedRepository, times(1)).findByCountry("대한민국");
        verify(feedRepository, times(1)).findByCountryNot("대한민국");
    }

    @Test
    void findPublicHomeDataSuccessTest() {
        when(feedRepository.findByCountry(anyString())).thenReturn(List.of(feed));
        when(feedRepository.findByCountryNot(anyString())).thenReturn(List.of(feed));

        HomeResponseDto response = feedService.findPublicHomeData();

        assertThat(response).isNotNull();
        verify(feedRepository, times(1)).findByCountry("대한민국");
        verify(feedRepository, times(1)).findByCountryNot("대한민국");
    }

    @Test
    void deleteDaysSuccessTest() {
        // Given
        Days mockDays = new Days();
        ReflectionTestUtils.setField(mockDays, "id", 1L);

        when(daysRepository.findByIdWithFeed(anyLong(), anyLong())).thenReturn(Optional.of(mockDays));
        when(feedRepository.findById(anyLong())).thenReturn(Optional.of(feed));

        doNothing().when(daysRepository).deleteById(anyLong());

        // When
        feedService.deleteDays(1L, 1L);

        // Then
        verify(daysRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteActivitySuccessTest() {
        // Given
        Activity mockActivity = new Activity();
        ReflectionTestUtils.setField(mockActivity, "id", 1L);

        when(activityRepository.findByIdWithFeedAndDays(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(mockActivity));
        when(feedRepository.findById(anyLong())).thenReturn(Optional.of(feed));

        doNothing().when(activityRepository).deleteById(anyLong());

        // When
        feedService.deleteActivity(1L, 1L, 1L);

        // Then
        verify(activityRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void createDaySuccessTest() {
        when(feedRepository.findById(anyLong())).thenReturn(Optional.of(feed));
        when(daysRepository.save(any(Days.class))).thenReturn(mock(Days.class));

        feedService.createDay(1L, new DaysRequestDto(LocalDate.now(), List.of()));

        verify(daysRepository, times(1)).save(any(Days.class));
    }

    @Test
    void createActivitySuccessTest() {
        when(daysRepository.findByIdWithFeed(anyLong(), anyLong())).thenReturn(Optional.of(mock(Days.class)));

        when(feedRepository.findById(anyLong())).thenReturn(Optional.of(feed));
        when(activityRepository.save(any(Activity.class))).thenReturn(mock(Activity.class));

        feedService.createActivity(1L, 1L, new ActivityRequestDto("Test Title", 5, "Test Memo", "Seoul", 37.5665, 126.9780));

        verify(activityRepository, times(1)).save(any(Activity.class));
    }
}
