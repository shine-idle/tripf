package com.shineidle.tripf.domain.photo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PhotoRequestDto {
    @NotNull(message = "사진에 대한 설명을 입력해주세요.")
    private final String description;
}