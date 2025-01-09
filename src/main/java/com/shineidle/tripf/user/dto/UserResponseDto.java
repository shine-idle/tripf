package com.shineidle.tripf.user.dto;

import com.shineidle.tripf.user.type.UserAuth;
import com.shineidle.tripf.user.type.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String name;
    private final String address;
    private final UserStatus status;
    private final UserAuth auth;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final LocalDateTime deletedAt;
}
