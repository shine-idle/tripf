package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;

public interface ChatRoomService {
    ChatRoomResponseDto createOrGetRoom(ChatRoomRequestDto chatRoomRequestDto);

    ChatRoomResponseDto getRoom(String roomId);
}
