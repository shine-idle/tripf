package com.shineidle.tripf.chat.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("chat_message")
@Getter
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessage(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    protected ChatMessage() {}
}
