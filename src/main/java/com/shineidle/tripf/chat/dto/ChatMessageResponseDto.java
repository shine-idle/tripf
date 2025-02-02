package com.shineidle.tripf.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageResponseDto {
    private String content;
    private String sender;
    private String timestamp;

    public ChatMessageResponseDto(String content, String sender, String timestamp) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }
}
