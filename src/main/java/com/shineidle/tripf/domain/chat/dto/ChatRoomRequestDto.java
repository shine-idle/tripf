package com.shineidle.tripf.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomRequestDto {
    @NotBlank(message = "채팅방 이름은 비어있을 수 없습니다.")
    private String name;

    private List<String> users;
}
