package com.shineidle.tripf.chatbot.repository;

import com.shineidle.tripf.chatbot.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
    /**
     * 유저Id에 해당하는 챗봇 대화 기록 반환
     *
     * @param userId 유저 식별자
     * @return {@link Chatbot}
     */
    List<Chatbot> findByUserId(Long userId);
}