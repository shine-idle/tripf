package com.shineidle.tripf.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageResponseDto {
    private String content;
    private String senderEmail;
    private String timestamp;

    public ChatMessageResponseDto(String content, String senderEmail, String timestamp) {
        this.content = content;
        this.senderEmail = senderEmail;
        this.timestamp = timestamp;
    }
}
