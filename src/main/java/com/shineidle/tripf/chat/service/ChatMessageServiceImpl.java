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

/**
 * 채팅 메시지를 관리하는 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int BATCH_SIZE = 100;

    /**
     * 메시지를 저장하고 Redis 캐시에 추가
     *
     * @param roomId  메시지를 보낼 채팅방 Id
     * @param sender  메시지를 보낸 유저
     * @param content 메시지 내용
     * @return 저장된 메시지 객체
     */
    @Override
    public ChatMessage sendMessage(String roomId, String sender, String content) {
        ChatMessage message = new ChatMessage(roomId, sender, content);
        chatMessageRepository.save(message);
        redisTemplate.opsForList().rightPush("chatMessages:" + message.getRoomId(), message);
        redisTemplate.expire("chatMessages:" + message.getRoomId(), Duration.ofHours(1));
        return message;
    }

    /**
     * 특정 채팅방의 메시지를 조회
     *
     * @param roomId        조회할 채팅방 Id
     * @param lastTimestamp 특정 시간 이후의 메시지를 가져오기 위한 타임스탬프 (옵션)
     * @param limit         조회할 메시지 개수
     * @return 조회된 메시지 리스트
     */
    @Override
    public List<ChatMessage> getMessages(String roomId, LocalDateTime lastTimestamp, int limit) {
        List<ChatMessage> messages = getMessagesFromRedis(roomId);

        if (messages.isEmpty()) {
            messages = getMessagesFromDBAndCache(roomId);
        }

        return filterAndSortMessages(messages, lastTimestamp, limit);
    }

    /**
     * 채팅방 Id에 해당하는 메시지를 Redis에서 조회
     *
     * @param roomId 채팅방 Id
     * @return Redis에서 조회한 채팅 메시지 리스트
     */
    private List<ChatMessage> getMessagesFromRedis(String roomId) {
        return Objects.requireNonNull(redisTemplate.opsForList()
                        .range("chatMessages:" + roomId, 0, -1))
                .stream()
                .map(object -> (ChatMessage) object)
                .toList();
    }

    /**
     * 채팅방 Id에 해당하는 메시지를 데이터베이스에서 조회하고, Redis에 캐싱
     *
     * @param roomId 채팅방 Id
     * @return 데이터베이스에서 조회한 후 Redis에 캐싱된 채팅 메시지 리스트
     */
    private List<ChatMessage> getMessagesFromDBAndCache(String roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId);
        for (int i = 0; i < messages.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, messages.size());
            redisTemplate.opsForList().rightPushAll("chatMessages:" + roomId, messages.subList(i, end));
        }
        redisTemplate.expire("chatMessages:" + roomId, Duration.ofHours(1));
        return messages;
    }

    /**
     * 채팅 메시지를 특정 시간 이전의 메시지만 필터링하고, 최신순으로 정렬하여 제한된 개수만 반환
     *
     * @param messages      필터링 및 정렬할 채팅 메시지 리스트
     * @param lastTimestamp 해당 시간 이전의 메시지만 필터링 (null 가능)
     * @param limit         반환할 최대 메시지 개수
     * @return 필터링 및 정렬된 채팅 메시지 리스트
     */
    private List<ChatMessage> filterAndSortMessages(List<ChatMessage> messages, LocalDateTime lastTimestamp, int limit) {
        return messages.stream()
                .filter(message -> lastTimestamp == null || message.getTimestamp().isBefore(lastTimestamp))
                .sorted(Comparator.comparing(ChatMessage::getTimestamp).reversed())
                .limit(limit)
                .toList();
    }
}