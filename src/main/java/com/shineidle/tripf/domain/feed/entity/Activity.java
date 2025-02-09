package com.shineidle.tripf.domain.feed.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.photo.entity.ActivityPhoto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`activity`")
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "days_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Days days;

    private String title;

    private Integer star;

    private String memo;

    private String city;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityPhoto> activityPhotos = new ArrayList<>();

    public Activity() {}

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Activity(Days Days, String title, Integer star, String memo, String city, Double latitude, Double longitude) {
        this.days = Days;
        this.title = title;
        this.star = star;
        this.memo = memo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
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
    public void update(String title, Integer star, String memo, String city, Double latitude, Double longitude) {
        this.title = title;
        this.star = star;
        this.memo = memo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getRepresentativePhotoUrl() {
        return activityPhotos.stream()
                .map(activityPhoto -> activityPhoto.getPhoto().getUrl())
                .findFirst()
                .orElse(null);
    }
}
