package com.shineidle.tripf.chat.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("chat_room")
@Getter
@Setter
public class ChatRoom {
    @Id
    private String id;
    private String name;
    private List<String> users;
    private LocalDateTime createdAt;

    public ChatRoom(String name, List<String> users, LocalDateTime createdAt) {
        this.name = name;
        this.users = users;
        this.createdAt = createdAt;
    }

    protected ChatRoom() {}
}
