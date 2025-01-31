package com.shineidle.tripf.feed.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityRequestDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    @Size(max = 20, message = "제목은 50자 이내로 입력해야 합니다.")
    private final String title;

    @Min(value = 1, message = "별점은 1 미만의 값은 입력할 수 없습니다.")
    @Max(value = 5, message = "별점은 5점 이내여야 합니다.")
    private final Integer star;

    private final String memo;

    @NotBlank(message = "도시는 비워둘 수 없습니다.")
    @Size(max = 20, message = "도시는 255자 이내로 입력해야 합니다.")
    private final String city;

    @NotNull(message = "위도는 비워둘 수 없습니다.")
    private final Double latitude;

    @NotNull(message = "경도는 비워둘 수 없습니다.")
    private final Double longitude;
}
