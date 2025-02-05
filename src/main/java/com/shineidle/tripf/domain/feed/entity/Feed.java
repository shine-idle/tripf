package com.shineidle.tripf.domain.feed.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "`feed`")
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String country;

    private String city;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String title;

    private String content;

    private Long cost;

    private String tag;

    private LocalDateTime deletedAt;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Feed() {}

    public Feed(User user, String country, String city, LocalDateTime startedAt, LocalDateTime endedAt, String title, String content, Long cost, String tag) {
        this.user = user;
        this.country = country;
        this.city = city;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.tag = tag;
        this.deletedAt = null;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void update(String country, String city, LocalDateTime startedAt, LocalDateTime endedAt, String title, String content, Long cost, String tag) {
        this.country = country;
        this.city = city;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.tag = tag;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

}