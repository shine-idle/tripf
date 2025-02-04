package com.shineidle.tripf.chatbot.controller;

import com.shineidle.tripf.chatbot.RedisChatbotService;
import com.shineidle.tripf.chatbot.dto.AdminChatbotAnswersResponseDto;
import com.shineidle.tripf.chatbot.dto.AdminChatbotQuestionsRequestDto;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/chatbots")
@RequiredArgsConstructor
public class AdminChatbotController {

    private final RedisChatbotService redisChatbotService;

    /**
     * 챗봇 질문 생성
     *
     * @param adminChatbotQuestionsRequestDto {@link AdminChatbotQuestionsRequestDto} 관리자용 챗봇 질문 요청 Dto
     * @return {@link PostMessageResponseDto} 질문 생성 문구
     */
    @Operation(summary = "챗봇 질문 생성")
    @PostMapping("/questions")
    public ResponseEntity<PostMessageResponseDto> saveQuestions(
            @Valid @RequestBody AdminChatbotQuestionsRequestDto adminChatbotQuestionsRequestDto
    ) {
        redisChatbotService.saveQuestions(adminChatbotQuestionsRequestDto.getCategory(), adminChatbotQuestionsRequestDto.getQuestions());

        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.CHATBOT_QUESTION_SUCCESS);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 챗봇 답변 생성
     *
     * @param adminChatbotAnswersResponseDto {@link AdminChatbotAnswersResponseDto} 관리자용 챗봇 답변 요청 Dto
     * @return {@link PostMessageResponseDto} 답변 생성 문구
     */
    @Operation(summary = "챗봇 답변 생성")
    @PostMapping("/answers")
    public ResponseEntity<PostMessageResponseDto> saveAnswers(
            @Valid @RequestBody AdminChatbotAnswersResponseDto adminChatbotAnswersResponseDto
    ) {
        redisChatbotService.saveAnswers(adminChatbotAnswersResponseDto.getCategory(), adminChatbotAnswersResponseDto.getAnswer());

        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.CHATBOT_ANSWER_SUCCESS);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
