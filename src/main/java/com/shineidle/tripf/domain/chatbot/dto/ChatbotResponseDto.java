package com.shineidle.tripf.domain.chatbot.dto;

import com.shineidle.tripf.domain.chatbot.entity.Chatbot;
import com.shineidle.tripf.domain.chatbot.type.ResponseStatus;
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