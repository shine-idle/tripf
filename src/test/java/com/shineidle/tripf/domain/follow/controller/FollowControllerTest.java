package com.shineidle.tripf.domain.follow.controller;

import com.shineidle.tripf.domain.follow.controller.FollowController;
import com.shineidle.tripf.domain.follow.dto.FollowResponseDto;
import com.shineidle.tripf.domain.follow.service.FollowService;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.type.UserAuth;
import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowControllerTest {
    @InjectMocks
    private FollowController followController;

    @Mock
    private FollowService followService;

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
        doNothing().when(followService).createFollow(2L);

        ResponseEntity<String> response = followController.createFollow(2L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("팔로잉 되었습니다", response.getBody());
        verify(followService, times(1)).createFollow(2L);
    }

    @Test
    void createFollow_InvalidUser() {
        doThrow(new IllegalArgumentException("User not found")).when(followService).createFollow(2L);

        ResponseEntity<String> response = followController.createFollow(2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void findFollowers() {
        FollowResponseDto followResponseDto = new FollowResponseDto(2L);
        when(followService.findFollowers()).thenReturn(Collections.singletonList(followResponseDto));

        ResponseEntity<List<FollowResponseDto>> response = followController.findFollowers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(2L, response.getBody().get(0).getUserId());
        verify(followService, times(1)).findFollowers();
    }

    @Test
    void findFollowings() {
        FollowResponseDto followResponseDto = new FollowResponseDto(2L);
        when(followService.findFollowings()).thenReturn(Collections.singletonList(followResponseDto));

        ResponseEntity<List<FollowResponseDto>> response = followController.findFollowings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(2L, response.getBody().get(0).getUserId());
        verify(followService, times(1)).findFollowings();
    }

    @Test
    void deleteFollowByFollowingId() {
        doNothing().when(followService).deleteFollowByFollowingId(2L);

        ResponseEntity<String> response = followController.deleteFollowByFollowingId(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("팔로우가 취소 되었습니다", response.getBody());
        verify(followService, times(1)).deleteFollowByFollowingId(2L);
    }

    @Test
    void deleteFollowByFollowingId_InvalidUser() {
        doThrow(new IllegalArgumentException("Follow not found")).when(followService).deleteFollowByFollowingId(2L);

        ResponseEntity<String> response = followController.deleteFollowByFollowingId(2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Follow not found", response.getBody());
    }

    @Test
    void deleteFollowByFollowerId() {
        doNothing().when(followService).deleteFollowByFollowerId(2L);

        ResponseEntity<String> response = followController.deleteFollowByFollowerId(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("팔로우가 취소 되었습니다", response.getBody());
        verify(followService, times(1)).deleteFollowByFollowerId(2L);
    }

    @Test
    void deleteFollowByFollowerId_InvalidUser() {
        doThrow(new IllegalArgumentException("Follow not found")).when(followService).deleteFollowByFollowerId(2L);

        ResponseEntity<String> response = followController.deleteFollowByFollowerId(2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Follow not found", response.getBody());
    }
}