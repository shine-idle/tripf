package com.shineidle.tripf.domain.chat.repository;

import com.shineidle.tripf.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    /**
     * 특정 유저가 포함된 채팅방 조회
     *
     * @param users 유저 목록
     * @return 채팅방
     */
    ChatRoom findByUsers(List<String> users);
}
