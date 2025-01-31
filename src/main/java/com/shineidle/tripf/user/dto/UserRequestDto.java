package com.shineidle.tripf.user.dto;

import com.shineidle.tripf.user.type.UserAuth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "이메일은 필수 입니다.")
    @Pattern(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$", message = "올바르지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상이며, 최대 20자 까지 입력할 수 있습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "대문자, 소문자, 숫자, 특수문자를 최소 1개 이상 포함해야 합니다.")
    private final String password;

    @NotBlank(message = "이름은 필수 입니다.")
    @Size(min = 2, max = 50, message = "최소 2자에서 최대 50자까지 입력할 수 있습니다.")
    private final String name;

    @NotBlank
    private final String address;

    @NotNull
    private final UserAuth auth;
}
