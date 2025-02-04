package com.shineidle.tripf.notification.repository;

import com.shineidle.tripf.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // TODO : 주석필요
    List<Notification> findByUserId(Long userId);
}
