package com.shineidle.tripf.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class DaysResponseDto {

    private final Long id;

    private final LocalDate date;

    private final List<ActivityResponseDto> activity;

}