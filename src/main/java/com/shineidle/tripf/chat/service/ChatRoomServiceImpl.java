package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.chat.entity.ChatRoom;
import com.shineidle.tripf.chat.repository.ChatRoomRepository;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.ChatErrorCode;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import com.shineidle.tripf.common.util.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

// TODO : javadoc 작성
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
    public ChatRoomResponseDto findRoom(String roomId) {
        ChatRoom room = (ChatRoom) redisUtils.getFromRedis("chatRoom:" + roomId);

        if (room == null) {
            room = chatRoomRepository.findById(roomId).orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));
            redisUtils.saveToRedis("chatRoom:" + roomId, room, Duration.ofHours(1));
        }

        return new ChatRoomResponseDto(room.getId(), room.getName(), room.getUsers());
    }

    @Override
    public List<ChatRoomResponseDto> findRooms() {
        List<ChatRoom> all = chatRoomRepository.findAll();
        return all.stream().
                map(chatRoom -> new ChatRoomResponseDto(
                        chatRoom.getId(),
                        chatRoom.getName(),
                        chatRoom.getUsers()
                ))
                .toList();
    }

    @Override
    public PostMessageResponseDto deleteRoom(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));
        chatRoomRepository.delete(room);
        return new PostMessageResponseDto(PostMessage.CHATROOM_DELETED);
    }
}
