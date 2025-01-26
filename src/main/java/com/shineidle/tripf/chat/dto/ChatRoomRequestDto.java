package com.shineidle.tripf.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomRequestDto {
    @NotBlank(message = "채팅방 이름은 비어있을 수 없습니다.")
    private String name;

    // 사용자 리스트가 요청에 포함되면 받을 수 있도록 설정
    private List<String> users;
}
