package com.shineidle.tripf.chat.service;

import com.shineidle.tripf.chat.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

// TODO : javadoc 작성
public interface ChatMessageService {
    ChatMessage sendMessage(String roomId, String sender, String content);

    List<ChatMessage> getMessages(String roomId, LocalDateTime lastTimestamp, int limit);
}
