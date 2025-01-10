package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.*;

public interface UserService {
    PostMessageResponseDto create(UserRequestDto dto);

    JwtResponseDto login(UserRequestDto dto);

    UserResponseDto find(Long userId);

    PostMessageResponseDto updatePassword(Long userId, PasswordUpdateRequestDto dto);

    PostMessageResponseDto updateName(Long userId, UsernameUpdateRequestDto dto);
}
