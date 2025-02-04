package com.shineidle.tripf.photo.entity;

import com.shineidle.tripf.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`photo`")
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * 원본 사진 이름
     */
    @Column(nullable = false, length = 50)
    private String originalFileName;

    /**
     * 저장 사진 이름
     */
    @Column(nullable = false, length = 50)
    private String storedFileName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false, length = 30)
    private String ext;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityPhoto> activityPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPhoto> productPhotos = new ArrayList<>();

    protected Photo() {}

    public Photo(String originalFileName, String storedFileName, String description, String url, Long size, String ext) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.description = description;
        this.url = url;
        this.size = size;
        this.ext = ext;
    }

    public void update(String description, String originalFilename) {
        this.description = description;
        this.originalFileName = originalFilename;
    }
}
