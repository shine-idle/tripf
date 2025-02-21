package com.shineidle.tripf.domain.photo.dto;

import com.shineidle.tripf.domain.photo.entity.Photo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PhotoResponseDto {
    private final Long id;

    private final String originalImageName;

    private final String description;

    private final String url;

    private final Long size;

    private final String ext;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public static PhotoResponseDto toDto(Photo photo) {
        return new PhotoResponseDto(
                photo.getId(),
                photo.getOriginalFileName(),
                photo.getDescription(),
                photo.getUrl(),
                photo.getSize(),
                photo.getExt(),
                photo.getCreatedAt(),
                photo.getModifiedAt()
        );
    }

    public static PhotoResponseDto toDto2(Long domainId, Photo photo) {
        return new PhotoResponseDto(
                domainId,
                photo.getOriginalFileName(),
                photo.getDescription(),
                photo.getUrl(),
                photo.getSize(),
                photo.getExt(),
                photo.getCreatedAt(),
                photo.getModifiedAt()
        );
    }
}