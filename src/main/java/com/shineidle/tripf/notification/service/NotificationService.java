package com.shineidle.tripf.notification.service;

import com.shineidle.tripf.notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    /**
     * 알림 조회
     */
    List<NotificationResponseDto> findNotification(Long userId);
}
