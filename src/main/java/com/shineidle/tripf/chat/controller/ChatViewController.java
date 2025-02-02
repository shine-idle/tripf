package com.shineidle.tripf.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatViewController {
    @GetMapping("/chat")
    public String getChatView() {
        return "chat/chat";
    }
}
