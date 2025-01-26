package com.shineidle.tripf.chat.controller;

import com.shineidle.tripf.chat.dto.ChatMessageRequestDto;
import com.shineidle.tripf.chat.dto.ChatMessageResponseDto;
import com.shineidle.tripf.chat.entity.ChatMessage;
import com.shineidle.tripf.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/sendMessage/{roomId}")
    public void sendMessage(
            @RequestBody ChatMessageRequestDto requestDto,
            @DestinationVariable String roomId
    ) {
        ChatMessage message = chatMessageService.sendMessage(
                roomId,
                "tester",
                requestDto.getContent()
        );

        simpMessagingTemplate.convertAndSend(
                "/topic/chat/" + roomId,
                new ChatMessageResponseDto(
                        message.getContent(),
                        message.getSender(),
                        message.getTimestamp().toString()
                )
        );
    }

    @GetMapping("/chat-room/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessages(
            @PathVariable String roomId,
            @RequestParam(required = false) LocalDateTime lastTimestamp,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<ChatMessage> messages = chatMessageService.getMessages(roomId, lastTimestamp, limit);
        List<ChatMessageResponseDto> responseDtos = messages.stream()
                .map(message -> new ChatMessageResponseDto(
                                        message.getContent(),
                                        message.getSender(),
                                        message.getTimestamp().toString()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
