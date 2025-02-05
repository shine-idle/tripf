package com.shineidle.tripf.domain.feed.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FeedRequestDto {

    @NotBlank(message = "도시는 비워둘 수 없습니다.")
    @Size(max = 20, message = "도시는 255자 이내로 입력해야 합니다.")
    private final String city;

    @NotNull(message = "여행 시작일자는 비워둘 수 없습니다.")
    private final LocalDateTime startedAt;

    @NotNull(message = "여행 종료일자는 비워둘 수 없습니다.")
    private final LocalDateTime endedAt;

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    @Size(max = 50, message = "제목은 50자 이내로 입력해야 합니다.")
    private final String title;

    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    @Size(max = 255, message = "내용은 255자 이내로 입력해야 합니다.")
    private final String content;

    @Digits(integer = 8, fraction = 0, message = "여행경비는 0~100,000,000원 사이여야 합니다.")
    private final Long cost;

    @Size(max = 20, message = "내용은 255자 이내로 입력해야 합니다.")
    private final String tag;

    private final List<DaysResponseDto> days;
}
