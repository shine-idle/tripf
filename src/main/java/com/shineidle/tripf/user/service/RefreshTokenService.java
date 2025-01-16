package com.shineidle.tripf.user.service;

import com.shineidle.tripf.user.entity.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken generateToken(Long userId, Authentication authentication, boolean isSocialLogin);

    void verifyExpiration(RefreshToken refreshToken);

    RefreshToken rotateRefreshToken(RefreshToken validRefreshToken, Authentication authentication);

    Optional<RefreshToken> findByToken(String refreshToken);

    void deleteToken(Long userId);
}