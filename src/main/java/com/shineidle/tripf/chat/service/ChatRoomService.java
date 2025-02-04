package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;

import java.util.List;

// TODO : javadoc 작성
public interface ChatRoomService {
    ChatRoomResponseDto createOrGetRoom(ChatRoomRequestDto chatRoomRequestDto);

    ChatRoomResponseDto findRoom(String roomId);

    List<ChatRoomResponseDto> findRooms();

    PostMessageResponseDto deleteRoom(String roomId);
}
