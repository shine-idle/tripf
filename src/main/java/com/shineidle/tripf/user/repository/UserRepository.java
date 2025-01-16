package com.shineidle.tripf.user.repository;

import com.shineidle.tripf.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 이메일로 유저 찾기
     *
     * @param email 유저 이메일
     * @return {@link User}
     */
    Optional<User> findByEmail(String email);

    /**
     * ProviderId로 유저 찾기 (소셜로그인)
     *
     * @param id providerId
     * @return {@link User}
     */
    Optional<User> findByProviderId(String id);
}
