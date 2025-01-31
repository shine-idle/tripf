package com.shineidle.tripf.chat.repository;

import com.shineidle.tripf.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findByUsers(List<String> userA);
}
