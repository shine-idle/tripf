package com.shineidle.tripf.domain.chat.service;

import com.shineidle.tripf.domain.chat.dto.ChatRoomRequestDto;
import com.shineidle.tripf.domain.chat.dto.ChatRoomResponseDto;
import com.shineidle.tripf.domain.chat.entity.ChatRoom;
import com.shineidle.tripf.domain.chat.repository.ChatRoomRepository;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.ChatErrorCode;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.global.common.message.type.PostMessage;
import com.shineidle.tripf.global.common.util.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 채팅방을 관리하는 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final RedisUtils redisUtils;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 새로운 채팅방을 생성하거나 기존 채팅방을 조회
     *
     * @param chatRoomRequestDto 채팅방 생성 요청 데이터
     * @return 생성된 채팅방 정보
     */
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

    /**
     * 특정 채팅방을 조회
     *
     * @param roomId 조회할 채팅방 Id
     * @return 조회된 채팅방 정보
     */
    @Override
    public ChatRoomResponseDto findRoom(String roomId) {
        ChatRoom room = (ChatRoom) redisUtils.getFromRedis("chatRoom:" + roomId);

        if (room == null) {
            room = chatRoomRepository.findById(roomId).orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));
            redisUtils.saveToRedis("chatRoom:" + roomId, room, Duration.ofHours(1));
        }

        return new ChatRoomResponseDto(room.getId(), room.getName(), room.getUsers());
    }

    /**
     * 모든 채팅방을 조회
     *
     * @return 채팅방 리스트
     */
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

    /**
     * 특정 채팅방을 삭제
     *
     * @param roomId 삭제할 채팅방 Id
     * @return 삭제 결과 메시지
     */
    @Override
    public PostMessageResponseDto deleteRoom(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));
        chatRoomRepository.delete(room);
        return new PostMessageResponseDto(PostMessage.CHATROOM_DELETED);
    }
}
