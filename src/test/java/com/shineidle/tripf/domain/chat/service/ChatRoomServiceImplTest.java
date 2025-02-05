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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private RedisUtils redisUtils;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Test
    void testCreateOrGetRoom_NewRoom() {
        // Given
        List<String> users = List.of("user1", "user2");
        ChatRoomRequestDto requestDto = new ChatRoomRequestDto();
        ChatRoom room = new ChatRoom("Room1", List.of("user1", "user2"), LocalDateTime.now());
        when(chatRoomRepository.findByUsers(any())).thenReturn(null); // No room found
        when(chatRoomRepository.save(any())).thenReturn(room);
        doNothing().when(redisUtils).saveToRedis(any(), any(), any());

        // When
        ChatRoomResponseDto response = chatRoomService.createOrGetRoom(requestDto);

        // Then
        verify(chatRoomRepository).save(any());
        verify(redisUtils).saveToRedis(any(), eq(room), any());
        assertThat(response.getName()).isEqualTo("Room1");
        assertThat(response.getUsers()).contains("user1", "user2");
    }

    @Test
    void testCreateOrGetRoom_ExistingRoom() {
        // Given
        ChatRoomRequestDto requestDto = new ChatRoomRequestDto();
        ChatRoom room = new ChatRoom("Room1", List.of("user1", "user2"), LocalDateTime.now());
        when(chatRoomRepository.findByUsers(any())).thenReturn(room);

        // When
        ChatRoomResponseDto response = chatRoomService.createOrGetRoom(requestDto);

        // Then
        verify(chatRoomRepository, never()).save(any());
        assertThat(response.getName()).isEqualTo("Room1");
        assertThat(response.getUsers()).contains("user1", "user2");
    }

    @Test
    void testFindRoom_FoundInRedis() {
        // Given
        String roomId = "room1";
        ChatRoom room = new ChatRoom("Room1", List.of("user1", "user2"), LocalDateTime.now());
        when(redisUtils.getFromRedis("chatRoom:" + roomId)).thenReturn(room);

        // When
        ChatRoomResponseDto response = chatRoomService.findRoom(roomId);

        // Then
        verify(redisUtils).getFromRedis("chatRoom:" + roomId);
        assertThat(response.getName()).isEqualTo("Room1");
        assertThat(response.getUsers()).contains("user1", "user2");
    }

    @Test
    void testFindRoom_NotFoundInRedis() {
        // Given
        String roomId = "room1";
        ChatRoom room = new ChatRoom("Room1", List.of("user1", "user2"), LocalDateTime.now());
        when(redisUtils.getFromRedis("chatRoom:" + roomId)).thenReturn(null);
        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));
        doNothing().when(redisUtils).saveToRedis(any(), any(), any());

        // When
        ChatRoomResponseDto response = chatRoomService.findRoom(roomId);

        // Then
        verify(redisUtils).saveToRedis("chatRoom:" + roomId, room, Duration.ofHours(1));
        assertThat(response.getName()).isEqualTo("Room1");
        assertThat(response.getUsers()).contains("user1", "user2");
    }

    @Test
    void testDeleteRoom() {
        // Given
        String roomId = "room1";
        ChatRoom room = new ChatRoom("Room1", List.of("user1", "user2"), LocalDateTime.now());
        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // When
        PostMessageResponseDto response = chatRoomService.deleteRoom(roomId);

        // Then
        verify(chatRoomRepository).delete(room);
        assertThat(response.getMessage()).isEqualTo(PostMessage.CHATROOM_DELETED.getMessage());
    }

    @Test
    void testDeleteRoom_NotFound() {
        // Given
        String roomId = "room1";
        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.empty());

        // When & Then
        GlobalException exception = assertThrows(GlobalException.class, () -> chatRoomService.deleteRoom(roomId));
        assertThat(exception.getMessage()).isEqualTo(ChatErrorCode.CHATROOM_NOT_FOUND.getMessage());
    }
}