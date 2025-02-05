package com.shineidle.tripf.domain.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminChatbotQuestionsRequestDto {
    @NotBlank(message = "질문 목록에 저장할 카테고리를 입력하세요.")
    private final String category;

    @NotEmpty(message = "질문 목록에 저장할 질문들을 입력해주세요.")
    private List<String> questions;
}