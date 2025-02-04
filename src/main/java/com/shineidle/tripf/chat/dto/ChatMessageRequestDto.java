package com.shineidle.tripf.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    @NotBlank(message = "값을 비워둘 수 없습니다.")
    @Size(max = 1000, message = "메시지는 1000자를 넘을 수 없습니다.")

    private String content;
}
