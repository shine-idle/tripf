package com.shineidle.tripf.domain.follow.service;

import com.shineidle.tripf.domain.follow.entity.Follow;
import com.shineidle.tripf.domain.follow.entity.FollowPk;
import com.shineidle.tripf.domain.follow.repository.FollowRepository;
import com.shineidle.tripf.domain.follow.service.FollowServiceImpl;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.service.UserService;
import com.shineidle.tripf.domain.user.type.UserAuth;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @InjectMocks
    private FollowServiceImpl followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserService userService;

    private User loginUser;
    private User otherUser;

    @BeforeEach
    void setUp() {
        loginUser = new User("loginUser@gmail.com",
                "Develop!1234",
                "loginUser",
                UserAuth.ADMIN,
                "Seoul");

        ReflectionTestUtils.setField(loginUser, "id", 1L);

        otherUser = new User("otherUser@gmail.com",
                "Develop!1234",
                "otherUser",
                UserAuth.ADMIN,
                "Osaka");

        ReflectionTestUtils.setField(otherUser, "id", 2L);

        UserDetailsImpl userDetails = new UserDetailsImpl(loginUser);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createFollow() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(false);

        assertDoesNotThrow(() -> followService.createFollow(2L));
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void createFollow_AlreadyFollowed() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(true);

        assertThrows(GlobalException.class, () -> followService.createFollow(2L));
    }

    @Test
    void deleteFollowByFollowingId() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(true);

        assertDoesNotThrow(() -> followService.deleteFollowByFollowingId(2L));
        verify(followRepository, times(1)).deleteById(any(FollowPk.class));
    }

    @Test
    void deleteFollowByFollowingId_NotExist() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(false);

        assertThrows(GlobalException.class, () -> followService.deleteFollowByFollowingId(2L));
    }

    @Test
    void deleteFollowByFollowerId() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(true);

        assertDoesNotThrow(() -> followService.deleteFollowByFollowerId(2L));
        verify(followRepository, times(1)).deleteById(any(FollowPk.class));
    }

    @Test
    void deleteFollowByFollowerId_NotExist() {
        when(userService.getUserById(1L)).thenReturn(loginUser);
        when(userService.getUserById(2L)).thenReturn(otherUser);
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(false);

        assertThrows(GlobalException.class, () -> followService.deleteFollowByFollowerId(2L));
    }
}