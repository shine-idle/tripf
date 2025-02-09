package com.shineidle.tripf.domain.notification.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.notification.type.NotifyType;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`notification`")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor;

    @Enumerated(EnumType.STRING)
    private NotifyType notifyType;

    private String notifyContext;

    private Long feedId;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    protected Notification() {}

    public static Notification create(User user, User actor, NotifyType notifyType, String notifyContext, Long feedId) {
        Notification notification = new Notification();
        notification.user = user;
        notification.actor = actor;
        notification.notifyType = notifyType;
        notification.notifyContext = notifyContext;
        notification.feedId = feedId;
        return notification;
    }
}