package com.shineidle.tripf.photo.dto;

import com.shineidle.tripf.photo.entity.Photo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PhotoCreateResponseDto {

    private final Long id;

    private final String originalImageName;

    private final String description;

    private final String url;

    private final Long size;

    private final String ext;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public static PhotoCreateResponseDto toDto(Photo photo) {
        return new PhotoCreateResponseDto(
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
}
