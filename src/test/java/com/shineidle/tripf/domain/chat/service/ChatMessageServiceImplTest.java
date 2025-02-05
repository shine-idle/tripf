package com.shineidle.tripf.domain.chat.service;

import com.shineidle.tripf.domain.chat.entity.ChatMessage;
import com.shineidle.tripf.domain.chat.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceImplTest {
    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Captor
    private ArgumentCaptor<ChatMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    @Test
    @DisplayName("메시지 전송 및 저장")
    void testSendMessage() {
        // Given
        String roomId = "room1";
        String sender = "user1";
        String content = "Hello!";
        ChatMessage message = new ChatMessage(roomId, sender, content);

        // When
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(message);

        ChatMessage savedMessage = chatMessageService.sendMessage(roomId, sender, content);

        // Then
        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.getRoomId()).isEqualTo(roomId);
        assertThat(savedMessage.getSender()).isEqualTo(sender);
        assertThat(savedMessage.getContent()).isEqualTo(content);

        verify(chatMessageRepository).save(messageCaptor.capture());
        verify(listOperations).rightPush(eq("chatMessages:" + roomId), any(ChatMessage.class));
        verify(redisTemplate).expire(eq("chatMessages:" + roomId), any());
    }

    @Test
    @DisplayName("Redis에서 메시지 조회")
    void testGetMessagesFromRedis() {
        // Given
        String roomId = "room1";
        ChatMessage message1 = new ChatMessage(roomId, "user1", "Hello!");
        ChatMessage message2 = new ChatMessage(roomId, "user2", "Hi!");

        // When
        when(listOperations.range("chatMessages:" + roomId, 0, -1)).thenReturn(List.of(message1, message2));

        List<ChatMessage> messages = chatMessageService.getMessages(roomId, null, 10);

        // Then
        assertThat(messages).hasSize(2);
        verify(listOperations).range("chatMessages:" + roomId, 0, -1);
    }

    @Test
    @DisplayName("DB에서 메시지 조회 및 Redis 캐싱")
    void testGetMessagesFromDBAndCache() {
        // Given
        String roomId = "room1";
        ChatMessage message = new ChatMessage(roomId, "user1", "Hello!");

        // When
        when(listOperations.range("chatMessages:" + roomId, 0, -1)).thenReturn(List.of());
        when(chatMessageRepository.findByRoomId(roomId)).thenReturn(List.of(message));

        List<ChatMessage> messages = chatMessageService.getMessages(roomId, null, 10);

        // Then
        assertThat(messages).hasSize(1);
        verify(chatMessageRepository).findByRoomId(roomId);
        verify(listOperations).rightPushAll(eq("chatMessages:" + roomId), Optional.ofNullable(any()));
    }
}
