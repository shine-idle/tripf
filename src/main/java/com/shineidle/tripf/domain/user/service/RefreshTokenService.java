package com.shineidle.tripf.domain.user.service;

import com.shineidle.tripf.domain.user.entity.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * 리프레시 토큰 생성
     *
     * @param userId         유저 Id
     * @param authentication 인증 정보
     * @param isSocialLogin  소셜 로그인 여부
     * @return 생성된 리프레시 토큰
     */
    RefreshToken generateToken(Long userId, Authentication authentication, boolean isSocialLogin);

    /**
     * 리프레시 토큰 값으로 리프레시 토큰 객체를 조회
     *
     * @param refreshToken 리프레시 토큰
     * @return 리프레시 토큰 객체 (Optional)
     */
    Optional<RefreshToken> findByToken(String refreshToken);
}