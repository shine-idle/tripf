package com.shineidle.tripf.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 채팅 뷰 담당 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class ChatViewController {
    /**
     * 채팅 뷰 랜더링
     *
     * @return chat.html
     */
    @GetMapping("/chat")
    public String getChatView() {
        return "chat/chat";
    }
}
