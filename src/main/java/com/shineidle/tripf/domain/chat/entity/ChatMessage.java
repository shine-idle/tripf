package com.shineidle.tripf.domain.chat.entity;

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

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public ChatMessage(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    protected ChatMessage() {}
}
