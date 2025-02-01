package com.shineidle.tripf.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatbotRequestDto {

    @NotBlank(message = "질문을 입력하세요.")
    private final String question;
}
