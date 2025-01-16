package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.*;

public interface UserService {
    PostMessageResponseDto createUser(UserRequestDto dto);

    JwtResponseDto login(UserRequestDto dto);

    UserResponseDto findUser(Long userId);

    PostMessageResponseDto updatePassword(PasswordUpdateRequestDto dto);

    PostMessageResponseDto updateName(UsernameUpdateRequestDto dto);

    PostMessageResponseDto deleteUser(UserRequestDto dto);

    void verify(UserRequestDto dto);

    JwtResponseDto updateToken(String refreshToken);

    void deleteRefreshToken();
}
