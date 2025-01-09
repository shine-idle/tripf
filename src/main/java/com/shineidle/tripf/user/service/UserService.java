package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.JwtResponseDto;
import com.shineidle.tripf.user.dto.UserRequestDto;

public interface UserService {
    PostMessageResponseDto create(UserRequestDto dto);

    JwtResponseDto login(UserRequestDto dto);
}
