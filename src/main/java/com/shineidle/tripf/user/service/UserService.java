package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.*;
import com.shineidle.tripf.user.entity.User;

public interface UserService {
    PostMessageResponseDto createUser(UserRequestDto dto);

    JwtResponseDto login(UserRequestDto dto);

    UserResponseDto findUser(Long userId);

    PostMessageResponseDto updatePassword(PasswordUpdateRequestDto dto);

    PostMessageResponseDto updateName(UsernameUpdateRequestDto dto);

    PostMessageResponseDto deleteUser(UserRequestDto dto);

    void verify(UserRequestDto dto);

    User getUserById(Long id);

}
