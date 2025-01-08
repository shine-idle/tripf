package com.shineidle.tripf.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FeedResponseDto {

    private final String city;

    private final LocalDateTime started_at;

    private final LocalDateTime ended_at;

    private final String title;

    private final String content;

    private final Long cost;

    private final String tag;

    private final LocalDateTime created_at;

    private final LocalDateTime updated_at;

    private final List<DaysResponseDto> daysResponseDto;

}
