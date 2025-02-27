package com.shineidle.tripf.domain.user.service;

import com.shineidle.tripf.domain.user.entity.RefreshToken;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.type.TokenType;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.UserErrorCode;
import com.shineidle.tripf.global.common.util.provider.JwtProvider;
import com.shineidle.tripf.global.common.util.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisUtils redisUtils;

    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";

    /**
     * 리프레시 토큰 생성
     *
     * @param userId         유저 식별자
     * @param authentication {@link Authentication} 인증 객체
     * @param isSocialLogin  소셜로그인 = true 또는 일반로그인 = false
     * @return {@link RefreshToken}
     */
    @Override
    @Transactional
    public RefreshToken generateToken(Long userId, Authentication authentication, boolean isSocialLogin) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new GlobalException(UserErrorCode.USER_NOT_FOUND));

        String token = jwtProvider.generateToken(authentication, isSocialLogin, TokenType.REFRESH);
        RefreshToken refreshToken = new RefreshToken(user, token, Instant.now().plusMillis(jwtProvider.getRefreshExpiryMillis()));

        String redisKey = REFRESH_TOKEN_PREFIX + refreshToken.getToken();
        redisUtils.saveToRedis(redisKey, refreshToken, Duration.ofMillis(jwtProvider.getRefreshExpiryMillis()));

        return refreshToken;
    }

    /**
     * 토큰으로 리프레시 토큰 조회
     *
     * @param token 조회할 리프레시 토큰
     * @return {@link RefreshToken}
     */
    public Optional<RefreshToken> findByToken(String token) {
        String key = REFRESH_TOKEN_PREFIX + token;
        RefreshToken refreshToken = (RefreshToken) redisUtils.getFromRedis(key);
        return Optional.ofNullable(refreshToken);
    }
}
