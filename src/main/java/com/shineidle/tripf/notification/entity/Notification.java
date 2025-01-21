package com.shineidle.tripf.notification.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.notification.type.NotifyType;
import com.shineidle.tripf.user.entity.User;
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