package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.entity.BaseEntity;
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

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Days() {}

    public Days(Feed feed, LocalDate date) {
        this.feed = feed;
        this.date = date;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public Days update(LocalDate date) {
        this.date = date;
        return this;
    }
}
