package com.shineidle.tripf.domain.chat.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomResponseDto {
    private String id;
    private String name;
    private List<String> users;

    public ChatRoomResponseDto(String id, String name, List<String> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }
}
