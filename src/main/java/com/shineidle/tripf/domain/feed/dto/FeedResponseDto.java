package com.shineidle.tripf.domain.feed.dto;

import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.photo.dto.PhotoResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FeedResponseDto {

    private final Long id;

    private final String representativePhotoUrl;

    private final String name;

    private final String city;

    private final LocalDateTime startedAt;

    private final LocalDateTime endedAt;

    private final String title;

    private final String content;

    private final Long cost;

    private final String tag;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final List<DaysResponseDto> days;

    public static FeedResponseDto toDto(Feed feed, List<DaysResponseDto> days) {

        String representativePhotoUrl = days.stream()
                .flatMap(day -> day.getActivity().stream())
                .flatMap(activity -> activity.getPhotos().stream())
                .map(PhotoResponseDto::getUrl)
                .findFirst()
                .orElse(null);

        return new FeedResponseDto(
                feed.getId(),
                representativePhotoUrl,
                feed.getUser().getName(),
                feed.getCity(),
                feed.getStartedAt(),
                feed.getEndedAt(),
                feed.getTitle(),
                feed.getContent(),
                feed.getCost(),
                feed.getTag(),
                feed.getCreatedAt(),
                feed.getModifiedAt(),
                days
        );
    }
}
