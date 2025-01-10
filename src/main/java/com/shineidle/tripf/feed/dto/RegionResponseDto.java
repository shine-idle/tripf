package com.shineidle.tripf.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RegionResponseDto {

    private final Long id;

    private final String city;

    private final String title;

    private final String content;

    private final LocalDateTime startedAt;

    private final LocalDateTime endedAt;

}
