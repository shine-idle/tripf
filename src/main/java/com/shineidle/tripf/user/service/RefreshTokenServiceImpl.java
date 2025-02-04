package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.UserErrorCode;
import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.user.type.TokenType;
import com.shineidle.tripf.user.entity.RefreshToken;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.RefreshTokenRepository;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    /**
     * 리프레시 토큰 생성
     *
     * @param userId 유저 식별자
     * @param authentication {@link Authentication} 인증 객체
     * @param isSocialLogin  소셜로그인 = true 또는 일반로그인 = false
     * @return {@link RefreshToken}
     */
    @Override
    @Transactional
    public RefreshToken generateToken(Long userId, Authentication authentication, boolean isSocialLogin) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new GlobalException(UserErrorCode.USER_NOT_FOUND));

        RefreshToken refreshToken = new RefreshToken(
                user,
                jwtProvider.generateToken(authentication, isSocialLogin, TokenType.REFRESH),
                Instant.now().plusMillis(jwtProvider.getRefreshExpiryMillis())
        );

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * 리프레시 토큰 유효성 확인
     *
     * @param refreshToken {@link RefreshToken}
     * @return {@link RefreshToken}
     */
    @Override
    @Transactional
    public void verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new GlobalException(UserErrorCode.EXPIRED_TOKEN);
        }
    }

    /**
     * 리프레시 토큰 갱신
     *
     * @param validRefreshToken {@link RefreshToken}
     * @param authentication {@link Authentication}}
     * @return {@link RefreshToken} 새 리프레시 토큰
     * @apiNote 기존의 리프레시 토큰을 삭제하고 새 리프레시 토큰을 저장
     */
    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken validRefreshToken, Authentication authentication) {
        refreshTokenRepository.delete(validRefreshToken);
        boolean isSocialLogin = validRefreshToken.getUser().getProvider() != null;
        return generateToken(validRefreshToken.getUser().getId(), authentication, isSocialLogin);
    }

    /**
     * 토큰으로 리프레시 토큰 조회
     *
     * @param token 조회할 리프레시 토큰
     * @return {@link RefreshToken}
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * 유저 식별자로 리프레시 토큰 조회
     * @param userId 유저 식별자
     * @return {@link RefreshToken}
     */
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    /**
     * 리프레시 토큰 삭제
     *
     * @param userId 유저 식별자
     */
    @Override
    @Transactional
    public void deleteToken(Long userId) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId).orElseThrow(() ->
                new GlobalException(UserErrorCode.TOKEN_NOT_FOUND));
        refreshTokenRepository.delete(token);
    }

    /**
     * 토큰과 유저 정보 삭제
     *
     * @param refreshToken {@link RefreshToken}
     */
    @Override
    @Transactional
    public void deleteTokenAndUser(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
        userRepository.delete(refreshToken.getUser());
    }
}
