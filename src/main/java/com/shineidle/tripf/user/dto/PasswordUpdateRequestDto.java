package com.shineidle.tripf.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordUpdateRequestDto {
    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    private final String password;

    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 최대 20자 까지 입력할 수 있습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "대문자, 소문자, 숫자, 특수문자를 최소 1개 이상 포함해야 합니다.")
    private final String newPassword;
}
