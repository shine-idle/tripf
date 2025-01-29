package com.shineidle.tripf.feed.dto;

import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ActivityResponseDto {

    private final Long id;

    private final String title;

    private final Integer star;

    private final String memo;

    private final String city;

    private final Double latitude;

    private final Double longitude;

    private final List<PhotoResponseDto> photos;

    private final String representativePhotoUrl;

    public static ActivityResponseDto toDto(Activity activity, List<PhotoResponseDto> photoDtos, String representativePhotoUrl) {
        return new ActivityResponseDto(
                activity.getId(),
                activity.getTitle(),
                activity.getStar(),
                activity.getMemo(),
                activity.getCity(),
                activity.getLatitude(),
                activity.getLongitude(),
                photoDtos,
                representativePhotoUrl
        );
    }
}
