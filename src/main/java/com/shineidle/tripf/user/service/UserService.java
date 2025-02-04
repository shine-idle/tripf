package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.*;
import com.shineidle.tripf.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

// TODO : javadoc 작성
public interface UserService {
    PostMessageResponseDto createUser(UserRequestDto dto);

    JwtResponseDto login(UserRequestDto dto, HttpServletResponse response);

    UserResponseDto findUser(Long userId);

    PostMessageResponseDto updatePassword(PasswordUpdateRequestDto dto);

    PostMessageResponseDto updateName(UsernameUpdateRequestDto dto);

    PostMessageResponseDto deleteUser(UserRequestDto dto);

    void verify(UserRequestDto dto);

    JwtResponseDto updateToken(String refreshToken);

    void deleteRefreshToken();

    User getUserById(Long id);

    List<String> getActiveUserEmails();
}
