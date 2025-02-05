package com.shineidle.tripf.domain.chatbot.service;

import com.shineidle.tripf.domain.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.domain.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.domain.chatbot.dto.ChatbotResponseDto;

import java.util.List;

public interface ChatbotService {
    /**
     * 사용자 질문 입력 및 챗봇 답변 생성
     *
     * @param chatbotRequestDto {@link ChatbotRequestDto} 챗봇 요청 Dto
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto);

    /**
     * 질문 목록 조회
     *
     * @return {@link ChatbotQuestionsResponseDto} 챗봇 질문 목록 응답 Dto
     */
    List<ChatbotQuestionsResponseDto> findAllChatbotQuestion();

    /**
     * 사용자 대화 기록 조회
     *
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    List<ChatbotResponseDto> findConversationLogs();
}