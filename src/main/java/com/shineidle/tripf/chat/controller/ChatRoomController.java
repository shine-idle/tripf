package com.shineidle.tripf.chat.controller;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestBody ChatRoomRequestDto chatRoomRequestDto
    ) {
        return new ResponseEntity<>(chatRoomService.createOrGetRoom(chatRoomRequestDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDto>> findChatRooms() {
        return new ResponseEntity<>(chatRoomService.findRooms(), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> findChatRoom(
            @PathVariable String roomId
    ) {
        return new ResponseEntity<>(chatRoomService.findRoom(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(
            @PathVariable String roomId
    ) {
        chatRoomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
