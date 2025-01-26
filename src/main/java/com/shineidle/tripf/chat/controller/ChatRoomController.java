package com.shineidle.tripf.chat.controller;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoom(
            @PathVariable String roomId
    ) {
        return new ResponseEntity<>(chatRoomService.getRoom(roomId), HttpStatus.OK);
    }
}
