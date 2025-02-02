package com.shineidle.tripf.follow.service;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.follow.dto.FollowResponseDto;
import com.shineidle.tripf.follow.entity.Follow;
import com.shineidle.tripf.follow.entity.FollowPk;
import com.shineidle.tripf.follow.repository.FollowRepository;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import com.shineidle.tripf.user.type.UserAuth;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @InjectMocks
    private FollowServiceImpl followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    private User loginUser;
    private User otherUser;

    @BeforeEach
    void setUp() {
        loginUser = new User("loginUser@gmail.com",
                "Develop!1234",
                "loginUser",
                UserAuth.ADMIN,
                "Seoul"
        );
        ReflectionTestUtils.setField(loginUser, "id", 1L);

        otherUser = new User("otherUser@gmail.com",
                "Develop!1234",
                "otherUser",
                UserAuth.ADMIN,
                "Osaka"
        );
        ReflectionTestUtils.setField(otherUser, "id", 2L);

        UserDetailsImpl userDetails = new UserDetailsImpl(loginUser);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createFollow() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(loginUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(false);

        assertDoesNotThrow(() -> followService.createFollow(2L));
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void findFollowers() {
        Follow follow = new Follow(otherUser, loginUser);
        when(followRepository.findByFollowingId(loginUser)).thenReturn(Collections.singletonList(follow));

        List<FollowResponseDto> followers = followService.findFollowers();
        assertEquals(1, followers.size());
        assertEquals(2L, followers.get(0).getUserId());
    }

    @Test
    void findFollowings() {
        Follow follow = new Follow(loginUser, otherUser);
        when(followRepository.findByFollowerId(loginUser)).thenReturn(Collections.singletonList(follow));

        List<FollowResponseDto> followings = followService.findFollowings();
        assertEquals(1, followings.size());
        assertEquals(2L, followings.get(0).getUserId());
    }

    @Test
    void deleteFollowByFollowingId() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(true);

        assertDoesNotThrow(() -> followService.deleteFollowByFollowingId(2L));
        verify(followRepository, times(1)).deleteById(any(FollowPk.class));
    }

    @Test
    void deleteFollowByFollowerId() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(followRepository.existsById(any(FollowPk.class))).thenReturn(true);

        assertDoesNotThrow(() -> followService.deleteFollowByFollowerId(2L));
        verify(followRepository, times(1)).deleteById(any(FollowPk.class));
    }
}