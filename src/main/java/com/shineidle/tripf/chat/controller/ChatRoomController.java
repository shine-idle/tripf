package com.shineidle.tripf.chat.controller;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.chat.service.ChatRoomService;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채팅방을 관리하는 REST 컨트롤러
 */
@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /**
     * 새로운 채팅방을 생성하거나 기존 채팅방을 조회
     * @param chatRoomRequestDto 채팅방 생성 요청 데이터
     * @return 생성된 채팅방 정보
     */
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestBody ChatRoomRequestDto chatRoomRequestDto
    ) {
        return new ResponseEntity<>(chatRoomService.createOrGetRoom(chatRoomRequestDto), HttpStatus.OK);
    }

    /**
     * 모든 채팅방을 조회
     * @return 채팅방 리스트
     */
    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDto>> findChatRooms() {
        return new ResponseEntity<>(chatRoomService.findRooms(), HttpStatus.OK);
    }

    /**
     * 특정 채팅방을 조회
     * @param roomId 조회할 채팅방 Id
     * @return 조회된 채팅방 정보
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> findChatRoom(
            @PathVariable String roomId
    ) {
        return new ResponseEntity<>(chatRoomService.findRoom(roomId), HttpStatus.OK);
    }

    /**
     * 특정 채팅방을 삭제
     * @param roomId 삭제할 채팅방 Id
     * @return 삭제 결과 메시지
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<PostMessageResponseDto> deleteChatRoom(
            @PathVariable String roomId
    ) {
        return new ResponseEntity<>(chatRoomService.deleteRoom(roomId), HttpStatus.OK);
    }
}
