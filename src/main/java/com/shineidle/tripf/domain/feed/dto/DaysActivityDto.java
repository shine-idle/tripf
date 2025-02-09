package com.shineidle.tripf.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DaysActivityDto {
    private Long daysId;
    private LocalDate date;
    private Long activityId;
    private String title;
    private Integer star;
    private String memo;
    private String city;
    private Double latitude;
    private Double longitude;
}
