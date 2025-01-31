package com.shineidle.tripf.chatbot.dto;

import com.shineidle.tripf.cart.dto.CartResponseDto;
import com.shineidle.tripf.cart.entity.Cart;
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

    public static ChatbotResponseDto toDto(Chatbot chatbot) {
        return new ChatbotResponseDto(
                chatbot.getQuestion(),
                chatbot.getAnswer(),
                chatbot.getResponseStatus(),
                chatbot.getCreatedAt()
        );
    }
}
