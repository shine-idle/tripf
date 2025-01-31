package com.shineidle.tripf.feed.dto;

import com.shineidle.tripf.feed.entity.Feed;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RegionResponseDto {

    private final Long id;

    private final String country;

    private final String city;

    private final String title;

    private final String content;

    private final LocalDateTime startedAt;

    private final LocalDateTime endedAt;

    private final String representativePhotoUrl;

    public static RegionResponseDto toDto(Feed feed, String representativePhotoUrl) {
        return new RegionResponseDto(
                feed.getId(),
                feed.getCountry(),
                feed.getCity(),
                feed.getTitle(),
                feed.getContent(),
                feed.getStartedAt(),
                feed.getEndedAt(),
                representativePhotoUrl
        );
    }

}
