package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    private LocalDateTime started_at;

    private LocalDateTime ended_at;

    private String title;

    private String content;

    private Integer star;

    private String tag;

    private LocalDateTime deleted_at;
}
