package com.shineidle.tripf.user.repository;

import com.shineidle.tripf.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
