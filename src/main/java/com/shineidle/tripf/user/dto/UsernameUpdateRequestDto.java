package com.shineidle.tripf.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UsernameUpdateRequestDto {
    @NotBlank(message = "이름은 필수 입니다.")
    @Size(min = 2, max = 50, message = "최소 2자에서 최대 50자까지 입력할 수 있습니다.")
    private final String name;
}
