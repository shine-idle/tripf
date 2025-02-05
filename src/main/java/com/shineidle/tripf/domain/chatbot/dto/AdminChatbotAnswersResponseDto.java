package com.shineidle.tripf.domain.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminChatbotAnswersResponseDto {
    @NotBlank(message = "답변 목록에 저장할 카테고리를 입력하세요.")
    private final String category;

    @NotBlank(message = "답변 목록에 저장할 답변을 입력해주세요.")
    private final String answer;
}