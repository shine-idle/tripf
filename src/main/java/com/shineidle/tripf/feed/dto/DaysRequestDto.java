package com.shineidle.tripf.feed.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DaysRequestDto {

    @NotNull(message = "일정은 비워둘 수 없습니다.")
    private final LocalDate date;

    private final List<ActivityRequestDto> activity;
}
