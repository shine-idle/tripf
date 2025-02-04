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

/**
 * 채팅 메시지를 처리하는 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    /**
     * 특정 채팅방에 메시지를 전송하는 WebSocket 엔드포인트
     *
     * @param requestDto 전송할 메시지의 데이터
     * @param roomId     메시지를 보낼 채팅방 Id
     */
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

    /**
     * 특정 채팅방의 메시지를 조회하는 HTTP 엔드포인트
     *
     * @param roomId        조회할 채팅방 Id
     * @param lastTimestamp 특정 시간 이후의 메시지를 가져오기 위한 타임스탬프 (옵션)
     * @param limit         조회할 메시지 개수 (기본값 20)
     * @return 조회된 메시지 리스트
     */
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
