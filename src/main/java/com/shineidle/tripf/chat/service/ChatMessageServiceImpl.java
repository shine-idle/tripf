package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.entity.ChatMessage;
import com.shineidle.tripf.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int BATCH_SIZE = 100;


    @Override
    public ChatMessage sendMessage(String roomId, String sender, String content) {
        ChatMessage message = new ChatMessage(roomId, sender, content);
        chatMessageRepository.save(message);
        redisTemplate.opsForList().rightPush("chatMessages:" + message.getRoomId(), message);
        redisTemplate.expire("chatMessages:" + message.getRoomId(), Duration.ofHours(1));
        return message;
    }

    @Override
    public List<ChatMessage> getMessages(String roomId, LocalDateTime lastTimestamp, int limit) {
        // Redis에서 메시지 조회
        List<ChatMessage> messages = getMessagesFromRedis(roomId);

        if (messages.isEmpty()) {
            // Redis에 없으면 DB에서 조회 후 Redis에 캐싱
            messages = getMessagesFromDBAndCache(roomId);
        }

        return filterAndSortMessages(messages, lastTimestamp, limit);
    }

    private List<ChatMessage> getMessagesFromRedis(String roomId) {
        return Objects.requireNonNull(redisTemplate.opsForList()
                        .range("chatMessages:" + roomId, 0, -1))
                .stream()
                .map(object -> (ChatMessage) object)
                .toList();
    }

    private List<ChatMessage> getMessagesFromDBAndCache(String roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId);
        for (int i = 0; i < messages.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, messages.size());
            redisTemplate.opsForList().rightPushAll("chatMessages:" + roomId, messages.subList(i, end));
        }
        redisTemplate.expire("chatMessages:" + roomId, Duration.ofHours(1));
        return messages;
    }

    private List<ChatMessage> filterAndSortMessages(List<ChatMessage> messages, LocalDateTime lastTimestamp, int limit) {
        return messages.stream()
                .filter(message -> lastTimestamp == null || message.getTimestamp().isBefore(lastTimestamp))
                .sorted(Comparator.comparing(ChatMessage::getTimestamp).reversed())
                .limit(limit)
                .toList();
    }
}