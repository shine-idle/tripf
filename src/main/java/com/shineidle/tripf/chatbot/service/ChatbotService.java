package com.shineidle.tripf.chatbot.service;

import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;

import java.util.List;

public interface ChatbotService {
    ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto);

    List<ChatbotQuestionsResponseDto> findAllChatbotQuestion();
}
