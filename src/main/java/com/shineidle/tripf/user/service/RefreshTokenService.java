package com.shineidle.tripf.user.service;

import com.shineidle.tripf.user.entity.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

// TODO : javadoc 작성
public interface RefreshTokenService {
    RefreshToken generateToken(Long userId, Authentication authentication, boolean isSocialLogin);

    void verifyExpiration(RefreshToken refreshToken);

    RefreshToken rotateRefreshToken(RefreshToken validRefreshToken, Authentication authentication);

    Optional<RefreshToken> findByToken(String refreshToken);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteToken(Long userId);

    void deleteTokenAndUser(RefreshToken refreshToken);
}