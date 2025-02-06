package com.shineidle.tripf.domain.chatbot.repository;

import com.shineidle.tripf.domain.chatbot.entity.Chatbot;
import com.shineidle.tripf.domain.chatbot.type.ResponseStatus;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatbotRepositoryTest {
    @Autowired
    private ChatbotRepository chatbotRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Chatbot chatbot;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", "User name", UserAuth.NORMAL, "Address");
        userRepository.save(user);

        chatbot = new Chatbot(
                "안녕하세요",
                "안녕하세요! 무엇을 도와드릴까요?",
                ResponseStatus.SUCCESS,
                LocalDateTime.now(),
                user
        );
        chatbotRepository.save(chatbot);
    }

    @Test
    void findByUserIdSuccessTest() {
        List<Chatbot> foundChatbots = chatbotRepository.findByUserId(user.getId());

        assertNotNull(foundChatbots);
        assertFalse(foundChatbots.isEmpty());
        assertEquals(1, foundChatbots.size());
        assertEquals(chatbot.getQuestion(), foundChatbots.get(0).getQuestion());
        assertEquals(chatbot.getAnswer(), foundChatbots.get(0).getAnswer());

    }

    @Test
    void findByUserIdNotFoundTest() {
        List<Chatbot> foundChatbots = chatbotRepository.findByUserId(999L);

        assertNotNull(foundChatbots);
        assertTrue(foundChatbots.isEmpty());
    }

}