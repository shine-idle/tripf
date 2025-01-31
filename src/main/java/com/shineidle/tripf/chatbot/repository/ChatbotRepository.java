package com.shineidle.tripf.chatbot.repository;

import com.shineidle.tripf.chatbot.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
}
