package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.chat.entity.ChatRoom;
import com.shineidle.tripf.chat.repository.ChatRoomRepository;
import com.shineidle.tripf.common.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final RedisUtils redisUtils;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoomResponseDto createOrGetRoom(ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoom room = chatRoomRepository.findByUsers(chatRoomRequestDto.getUsers());
        if (room == null) {
            room = new ChatRoom(
                    chatRoomRequestDto.getName(),
                    chatRoomRequestDto.getUsers(),
                    LocalDateTime.now()
            );

            room = chatRoomRepository.save(room);
            redisUtils.saveToRedis("chatRoom:" + room.getId(), room, Duration.ofHours(1));
        }

        return new ChatRoomResponseDto(room.getId(), room.getName(), room.getUsers());
    }

    @Override
    public ChatRoomResponseDto getRoom(String roomId) {
        ChatRoom room = (ChatRoom) redisUtils.getFromRedis("chatRoom:" + roomId);

        if (room == null) {
            room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
            redisUtils.saveToRedis("chatRoom:" + roomId, room, Duration.ofHours(1));
        }

        return new ChatRoomResponseDto(room.getId(), room.getName(), room.getUsers());
    }
}
