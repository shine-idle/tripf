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
     * 토큰의 만료 여부 확인
     *
     * @param refreshToken 리프레시 토큰
     */
    void verifyExpiration(RefreshToken refreshToken);

    /**
     * 유요한 리프레시 토큰을 기반으로 새로운 리프레시 토큰을 생성
     *
     * @param validRefreshToken 유효한 리프레시 토큰
     * @param authentication    인증 정보
     * @return 새로운 리프레시 토큰
     */
    RefreshToken rotateRefreshToken(RefreshToken validRefreshToken, Authentication authentication);

    /**
     * 리프레시 토큰 값으로 리프레시 토큰 객체를 조회
     *
     * @param refreshToken 리프레시 토큰
     * @return 리프레시 토큰 객체 (Optional)
     */
    Optional<RefreshToken> findByToken(String refreshToken);

    /**
     * 유저 Id로 리프레시 토큰 조회
     *
     * @param userId 유저Id
     * @return 리프레시 토큰 객체 (Optional)
     */
    Optional<RefreshToken> findByUserId(Long userId);

    /**
     * 특정 유저의 리프레시 토큰 삭제
     *
     * @param userId 유저 Id
     */
    void deleteToken(Long userId);

    /**
     * 리프레시 토큰과 유저를 모두 삭제
     *
     * @param refreshToken 리프레시 토큰 객체
     */
    void deleteTokenAndUser(RefreshToken refreshToken);
}