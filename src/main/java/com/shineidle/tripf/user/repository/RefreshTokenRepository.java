package com.shineidle.tripf.user.repository;

import com.shineidle.tripf.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * 토큰값을 기반으로 리프레시 토큰을 조회
     *
     * @param token 토큰
     * @return 리프레시 토큰 (Optional)
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * 유저 Id로 리프레시 토큰을 조회
     *
     * @param userId 유저 Id
     * @return 리프레시 토큰 (Optional)
     */
    Optional<RefreshToken> findByUserId(Long userId);
}
