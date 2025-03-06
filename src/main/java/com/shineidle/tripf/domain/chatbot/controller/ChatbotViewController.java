package com.shineidle.tripf.domain.chatbot.controller;

import com.shineidle.tripf.domain.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.domain.chatbot.dto.ChatbotResponseDto;
import com.shineidle.tripf.domain.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatbotViewController {
    private final ChatbotService chatbotService;

    /**
     * 챗봇 메인 페이지 조회
     *
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 챗봇 메인 페이지 뷰 이름
     */
    @Operation(summary = "챗봇 메인 페이지 조회")
    @GetMapping("/chatbot")
    public String getChatbotPage(Model model) {
        List<ChatbotQuestionsResponseDto> questions = chatbotService.findAllChatbotQuestion();
        List<ChatbotResponseDto> conversationLogs = chatbotService.findConversationLogs();
        
        model.addAttribute("questions", questions);
        model.addAttribute("conversationLogs", conversationLogs);
        
        return "chatbot/chat";
    }

    /**
     * 챗봇 관리자 페이지 조회
     *
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 챗봇 관리자 페이지 뷰 이름
     */
    @Operation(summary = "챗봇 관리자 페이지 조회")
    @GetMapping("/admin/chatbot")
    public String getAdminChatbotPage(Model model) {
        List<ChatbotQuestionsResponseDto> questions = chatbotService.findAllChatbotQuestion();
        model.addAttribute("questions", questions);
        
        return "chatbot/admin";
    }
} 