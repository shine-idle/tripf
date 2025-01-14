package com.shineidle.tripf.user.dto;

import com.shineidle.tripf.user.entity.User;
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
    private final String provider;
    private final String providerId;
    private final UserStatus status;
    private final UserAuth auth;
    private final String address;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final LocalDateTime deletedAt;

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getProvider(),
                user.getProviderId(),
                user.getStatus(),
                user.getAuth(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getDeletedAt()
        );
    }
}
