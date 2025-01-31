package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponseDto createOrGetRoom(ChatRoomRequestDto chatRoomRequestDto);

    ChatRoomResponseDto findRoom(String roomId);

    List<ChatRoomResponseDto> findRooms();

    void deleteRoom(String roomId);
}
