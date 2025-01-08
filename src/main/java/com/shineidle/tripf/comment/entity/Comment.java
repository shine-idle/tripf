package com.shineidle.tripf.comment.entity;

import com.shineidle.tripf.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`comment`")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    private String comment;
}
