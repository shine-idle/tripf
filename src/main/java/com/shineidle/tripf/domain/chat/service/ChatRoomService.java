package com.shineidle.tripf.domain.chat.service;

import com.shineidle.tripf.domain.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.domain.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;

import java.util.List;

public interface ChatRoomService {
    /**
     * 새로운 채팅방을 생성하거나 기존 채팅방을 조회
     *
     * @param chatRoomRequestDto 채팅방 생성 요청 데이터
     * @return 생성된 채팅방 정보
     */
    ChatRoomResponseDto createOrGetRoom(ChatRoomRequestDto chatRoomRequestDto);

    /**
     * 특정 채팅방을 조회
     *
     * @param roomId 조회할 채팅방 Id
     * @return 조회된 채팅방 정보
     */
    ChatRoomResponseDto findRoom(String roomId);

    /**
     * 모든 채팅방을 조회
     *
     * @return 채팅방 리스트
     */
    List<ChatRoomResponseDto> findRooms();

    /**
     * 채팅방 삭제
     *
     * @param roomId 삭제할 채팅방 Id
     * @return 삭제 결과 메시지
     */
    PostMessageResponseDto deleteRoom(String roomId);
}
