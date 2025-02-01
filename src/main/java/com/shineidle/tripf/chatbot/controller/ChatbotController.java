package com.shineidle.tripf.chatbot.controller;

import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;
import com.shineidle.tripf.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatbots")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    /**
     * 사용자 질문 입력 및 챗봇 답변 생성
     *
     * @param chatbotRequestDto {@link ChatbotRequestDto} 챗봇 요청 Dto
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    @Operation(summary = "사용자 질문 입력 및 챗봇 답변 생성")
    @PostMapping("/response")
    public ResponseEntity<ChatbotResponseDto> createChatbotResponse(
            @Valid @RequestBody ChatbotRequestDto chatbotRequestDto
    ) {
        ChatbotResponseDto responseDto = chatbotService.createChatbotResponse(chatbotRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 사용자 대화 기록 조회
     *
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    @Operation(summary = "사용자 대화 기록 조회")
    @GetMapping("/log")
    public ResponseEntity<List<ChatbotResponseDto>> findConversationLogs() {

        List<ChatbotResponseDto> responseDtos = chatbotService.findConversationLogs();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /**
     * 질문 목록 조회
     *
     * @return {@link ChatbotQuestionsResponseDto} 챗봇 질문 목록 응답 Dto
     */
    @Operation(summary = "질문 목록 조회")
    @GetMapping("/questions")
    public ResponseEntity<List<ChatbotQuestionsResponseDto>> findAllChatbotQuestion() {

        List<ChatbotQuestionsResponseDto> responseDtos = chatbotService.findAllChatbotQuestion();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
