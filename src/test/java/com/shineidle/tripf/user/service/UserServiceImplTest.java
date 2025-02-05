package com.shineidle.tripf.user.service;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.UserErrorCode;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.global.common.message.type.PostMessage;
import com.shineidle.tripf.domain.user.dto.UserRequestDto;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.service.UserServiceImpl;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void createUser_Success() {
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "name", "address", null);
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        PostMessageResponseDto response = userService.createUser(requestDto);

        assertNotNull(response);
        assertEquals(PostMessage.SIGNUP_SUCCESS.getMessage(), response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_EmailAlreadyExists() {
        User existingUser = new User("test@example.com", "encodedPassword", "name", null, "address");
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.createUser(new UserRequestDto("test@example.com", "password", "name", "address", UserAuth.NORMAL)));

        assertEquals(UserErrorCode.EMAIL_DUPLICATED.getMessage(), exception.getMessage());
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.login(new UserRequestDto("test@example.com", "password", "name", "address", UserAuth.NORMAL), null));

        assertEquals(UserErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void login_UserDeactivated() {
        User deactivatedUser = new User("test@example.com", "encodedPassword", "name", null, "address");
        deactivatedUser.deactivate();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(deactivatedUser));

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.login(new UserRequestDto("test@example.com", "password", "name", "address", UserAuth.NORMAL), null));

        assertEquals(UserErrorCode.USER_DEACTIVATED.getMessage(), exception.getMessage());
    }
}
