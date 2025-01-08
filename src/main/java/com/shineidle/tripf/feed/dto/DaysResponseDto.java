package com.shineidle.tripf.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DaysResponseDto {

    private final LocalDate date;

    private final List<ActivityResponseDto> activityResponseDto;
}
