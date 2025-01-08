package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`activity`")
public class ActivityEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "days_id", nullable = false)
    private DaysEntity days;

    private String title;

    private Integer star;

    private String memo;

    private String city;

    private Double latitude;

    private Double longitude;
}
