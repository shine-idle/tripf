package com.shineidle.tripf.chatbot.dto;

import com.shineidle.tripf.chatbot.entity.Chatbot;
import com.shineidle.tripf.chatbot.type.ResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatbotResponseDto {
    private final String question;

    private final String answer;

    private final ResponseStatus responseStatus;

    private final LocalDateTime createdAt;

    // 기본 생성자 추가
    public ChatbotResponseDto() {
        this.question = null;
        this.answer = null;
        this.responseStatus = null;
        this.createdAt = null;
    }

    public static ChatbotResponseDto toDto(Chatbot chatbot) {
        return new ChatbotResponseDto(
                chatbot.getQuestion(),
                chatbot.getAnswer(),
                chatbot.getResponseStatus(),
                chatbot.getCreatedAt()
        );
    }
}