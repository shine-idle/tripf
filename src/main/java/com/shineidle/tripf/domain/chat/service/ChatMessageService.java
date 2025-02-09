package com.shineidle.tripf.domain.chat.service;

import com.shineidle.tripf.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageService {
    /**
     * 메시지 보내기
     *
     * @param roomId  메시지를 보낼 채팅방 Id
     * @param sender  보내는 사람
     * @param content 메시지 내용
     * @return 저장된 메시지 객체
     */
    ChatMessage sendMessage(String roomId, String sender, String content);

    /**
     * 메시지 조회
     *
     * @param roomId        조회할 채팅방 Id
     * @param lastTimestamp 특정 시간 이후의 메시지를 가져오기 위한 타임스탬프
     * @param limit         조회할 메시지 개수
     * @return 조회된 메시지 리스트
     */
    List<ChatMessage> getMessages(String roomId, LocalDateTime lastTimestamp, int limit);
}
