package com.shineidle.tripf.domain.user.dto;

import lombok.Getter;
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

    /**
     * refresh token
     */
    private final String refreshToken;
}
