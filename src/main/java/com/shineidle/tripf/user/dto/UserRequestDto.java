package com.shineidle.tripf.user.dto;

import com.shineidle.tripf.user.UserAuth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {
    private final String email;
    private final String password;
    private final String name;
    private final String address;
    private final UserAuth auth;
}
