package com.shineidle.tripf.chat.repository;

import com.shineidle.tripf.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    /**
     * 채팅방 Id로 채팅 메시지 조회
     *
     * @param roomId 채팅방 Id
     * @return 채팅메시지
     */
    List<ChatMessage> findByRoomId(String roomId);
}
