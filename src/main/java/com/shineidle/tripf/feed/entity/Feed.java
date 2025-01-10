package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.user.entity.User;
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

    public Feed(User user, String city, LocalDateTime startedAt, LocalDateTime endedAt, String title, String content, Long cost, String tag) {
        this.user = user;
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
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void update(String city, LocalDateTime startedAt, LocalDateTime endedAt, String title, String content, Long cost, String tag) {
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