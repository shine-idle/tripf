package com.shineidle.tripf.chatbot.service;

import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;

import java.util.List;
//TODO : javadoc
public interface ChatbotService {
    /**
     * 챗봇 응답을 생성
     */
    ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto);

    /**
     *챗봇 질문 목록 조회
     */
    List<ChatbotQuestionsResponseDto> findAllChatbotQuestion();

    /**
     *사용자 대화 기록 조회
     */
    List<ChatbotResponseDto> findConversationLogs();
}