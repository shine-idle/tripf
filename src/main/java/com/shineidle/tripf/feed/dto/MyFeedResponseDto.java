package com.shineidle.tripf.feed.dto;

import com.shineidle.tripf.feed.entity.Feed;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MyFeedResponseDto {
    private final Long id;

    private final String name;

    private final String country;

    private final String city;

    private final String title;

    private final String content;

    private final LocalDateTime startedAt;

    private final LocalDateTime endedAt;

    public static MyFeedResponseDto toDto(Feed feed) {
        return new MyFeedResponseDto (
                feed.getId(),
                feed.getUser().getName(),
                feed.getCountry(),
                feed.getCity(),
                feed.getTitle(),
                feed.getContent(),
                feed.getStartedAt(),
                feed.getEndedAt()
        );
    }
}
