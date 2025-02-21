package com.shineidle.tripf.domain.notification.service;

import com.shineidle.tripf.domain.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.domain.notification.type.NotifyType;
import com.shineidle.tripf.domain.user.entity.User;

import java.util.List;

public interface NotificationService {
    /**
     * 알림 조회
     */
    List<NotificationResponseDto> findNotification(Long userId);

    /**
     * 피드, 댓글, 좋아요 알림 생성
     */
    void createNotification(User targetUser, User actor, NotifyType notifyType, String context, Long feedId);
}
