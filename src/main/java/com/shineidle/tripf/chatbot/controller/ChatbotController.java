package com.shineidle.tripf.chatbot.controller;

import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;
import com.shineidle.tripf.chatbot.service.ChatbotService;
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

    // 사용자 질문 입력 및 챗봇 답변 생성
    @PostMapping("/response")
    public ResponseEntity<ChatbotResponseDto> createChatbotResponse(
            @Valid @RequestBody ChatbotRequestDto chatbotRequestDto
    ) {
        ChatbotResponseDto responseDto = chatbotService.createChatbotResponse(chatbotRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 사용자 대화 기록 조회

    // 질문 목록 조회
    @GetMapping("/question")
    public ResponseEntity<List<ChatbotQuestionsResponseDto>> findAllChatbotQuestion() {

        List<ChatbotQuestionsResponseDto> responseDtos = chatbotService.findAllChatbotQuestion();
        System.out.println("Response DTOs in Controller: " + responseDtos);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
