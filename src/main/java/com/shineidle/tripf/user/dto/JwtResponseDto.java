package com.shineidle.tripf.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtResponseDto {
    /**
     * access token 인증 방식
     */
    private final String tokenAuthScheme;

    /**
     * access token
     */
    private final String accessToken;
}
