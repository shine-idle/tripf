package com.shineidle.tripf.chatbot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ChatbotQuestionsResponseDto {
    private final String category;
    private List<String> question;

    public ChatbotQuestionsResponseDto(String category, List<String> question) {
        this.category = category;
        this.question = question;
    }
}