package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "`days`")
public class Days extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    private LocalDate date;

    public Days(Feed feed, LocalDate date) {
        this.feed = feed;
        this.date = date;
    }
}
