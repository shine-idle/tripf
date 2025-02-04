package com.shineidle.tripf.notification.repository;

import com.shineidle.tripf.comment.dto.CommentResponseDto;
import com.shineidle.tripf.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * UserId로 알림 리스트 조회
     *
     * @param userId 유저 식별자
     * @return {@link Notification}
     */
    List<Notification> findByUserId(Long userId);
}
