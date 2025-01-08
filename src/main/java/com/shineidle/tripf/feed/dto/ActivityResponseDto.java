package com.shineidle.tripf.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityResponseDto {

    private final String title;

    private final Integer star;

    private final String memo;

    private final String city;

    private final Double latitude;

    private final Double longitude;

}
