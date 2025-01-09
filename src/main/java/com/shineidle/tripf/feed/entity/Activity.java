package com.shineidle.tripf.feed.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.feed.dto.ActivityResponseDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`activity`")
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "days_id", nullable = false)
    private Days days;

    private String title;

    private Integer star;

    private String memo;

    private String city;

    private Double latitude;

    private Double longitude;

    public Activity() {}

    public Activity(Days Days, String title, Integer star, String memo, String city, Double latitude, Double longitude) {
        this.days = Days;
        this.title = title;
        this.star = star;
        this.memo = memo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(int star, String memo, String city, Double latitude, Double longitude) {
        this.star = star;
        this.memo = memo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
