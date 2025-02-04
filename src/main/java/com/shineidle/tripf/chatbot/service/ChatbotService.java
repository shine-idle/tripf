package com.shineidle.tripf.chatbot.service;

import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;

import java.util.List;
//TODO : javadoc
public interface ChatbotService {
    ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto);

    List<ChatbotQuestionsResponseDto> findAllChatbotQuestion();

    List<ChatbotResponseDto> findConversationLogs();
}