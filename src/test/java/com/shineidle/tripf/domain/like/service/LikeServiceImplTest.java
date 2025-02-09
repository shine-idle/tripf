package com.shineidle.tripf.domain.like.service;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.LikeErrorCode;
import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.feed.service.FeedService;
import com.shineidle.tripf.domain.like.entity.Like;
import com.shineidle.tripf.domain.like.entity.LikePk;
import com.shineidle.tripf.domain.like.repository.LikeRepository;
import com.shineidle.tripf.domain.like.service.LikeServiceImpl;
import com.shineidle.tripf.domain.notification.service.NotificationService;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.service.UserService;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private FeedService feedService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;


    private User user;
    private Feed feed;

    @BeforeEach
    void setUp() {
        user = new User("testUser@gmail.com",
                "Develop!1234",
                "testUser",
                UserAuth.ADMIN,
                "Seoul"
                );

        feed = new Feed(user,
                "Japan",
                "Osaka",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "title",
                "content",
                10000L,
                "Osaka Travel"
                );

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createLike_Success() {
        LikePk likePk = new LikePk(feed, user);
        when(feedService.checkFeed(feed.getId())).thenReturn(feed);
        when(likeRepository.existsById(likePk)).thenReturn(false);
        when(userService.getUserById(user.getId())).thenReturn(user);

        likeService.createLike(feed.getId());

        verify(likeRepository, times(1)).save(any(Like.class));
        verify(notificationService, times(1)).createNotification(any(), any(), any(), any(), any());
    }

    @Test
    void createLike_AlreadyLiked() {
        LikePk likePk = new LikePk(feed, user);
        when(feedService.checkFeed(feed.getId())).thenReturn(feed);
        when(likeRepository.existsById(likePk)).thenReturn(true);

        GlobalException exception = assertThrows(GlobalException.class, () -> likeService.createLike(feed.getId()));
        assertEquals(LikeErrorCode.ALREADY_LIKE.getMessage(), exception.getMessage());
    }

    @Test
    void deleteLike_Success() {
        LikePk likePk = new LikePk(feed, user);
        when(feedService.checkFeed(feed.getId())).thenReturn(feed);
        when(likeRepository.existsById(likePk)).thenReturn(true);

        likeService.deleteLike(feed.getId());

        verify(likeRepository, times(1)).deleteById(likePk);
    }

    @Test
    void deleteLike_NotLikedYet() {
        LikePk likePk = new LikePk(feed, user);
        when(feedService.checkFeed(feed.getId())).thenReturn(feed);
        when(likeRepository.existsById(likePk)).thenReturn(false);

        GlobalException exception = assertThrows(GlobalException.class, () -> likeService.deleteLike(feed.getId()));
        assertEquals(LikeErrorCode.LIKED_YET.getMessage(), exception.getMessage());
    }
}