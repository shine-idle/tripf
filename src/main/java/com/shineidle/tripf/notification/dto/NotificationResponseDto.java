package com.shineidle.tripf.notification.dto;

import com.shineidle.tripf.notification.entity.Notification;
import com.shineidle.tripf.notification.type.NotifyType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NotificationResponseDto {
    private final String actorName;

    private final String notifyContext;

    private final NotifyType notifyType;

    private final LocalDateTime createAt;

    private final Long feedId;

    public static NotificationResponseDto toDto(Notification notification) {
        return new NotificationResponseDto(
                notification.getActor().getName(),
                notification.getNotifyContext(),
                notification.getNotifyType(),
                notification.getCreatedAt(),
                notification.getFeedId()
        );
    }
}