package com.shineidle.tripf.chatbot.dto;

import com.shineidle.tripf.chatbot.entity.Chatbot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatbotQuestionsResponseDto {

    private String question;

    public ChatbotQuestionsResponseDto(String question) {
        System.out.println("Creating DTO with question: " + question);
        this.question = question;
    }

    @Override
    public String toString() {
        return "ChatbotQuestionsResponseDto{" +
                "question='" + question + '\'' +
                '}';
    }
}
