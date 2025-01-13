package com.shineidle.tripf.notification.service;

import com.shineidle.tripf.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.notification.entity.Notification;
import com.shineidle.tripf.notification.repository.NotificationRepository;
import com.shineidle.tripf.notification.type.NotifyType;
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 알림 조회
     * @param userId 유저 식별자
     * @return NotificationResponseDto
     */
    @Override
    public List<NotificationResponseDto> findNotification(Long userId) {

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(NotificationResponseDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 알림 생성 메서드
     * @param targetUser 알림을 받을 사용자 (대상)
     * @param actor 알림을 발생시킨 사용자 (발생자)
     * @param notifyType 알림 유형
     * @param context 알림 내용
     */
    public void createNotification(User targetUser, User actor, NotifyType notifyType, String context) {
        Notification notification = Notification.create(targetUser, actor, notifyType, context);
        notificationRepository.save(notification); // 저장
    }
}
