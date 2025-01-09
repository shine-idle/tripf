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

    // todo test를 위해 nullable = false 비활성화
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @JoinColumn(name = "user_id")
    private User user;

    private String city;

    private LocalDateTime started_at;

    private LocalDateTime ended_at;

    private String title;

    private String content;

    private Long cost;

    private String tag;

    private LocalDateTime deleted_at;

    public Feed() {}

    public Feed(String city, LocalDateTime started_at, LocalDateTime ended_at, String title, String content, Long cost, String tag) {
        this.city = city;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.tag = tag;
        this.deleted_at = LocalDateTime.now();
    }

    public void update(String city, LocalDateTime started_at, LocalDateTime ended_at, String title, String content, Long cost, String tag) {
        this.city = city;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.tag = tag;
        this.deleted_at = LocalDateTime.now();
    }
}