package com.shineidle.tripf.chatbot.entity;

import com.shineidle.tripf.chatbot.type.ResponseStatus;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "`chatbot`")
public class Chatbot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String question;

    private String answer;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    protected Chatbot() {}

    public Chatbot(String question, String answer, ResponseStatus responseStatus, LocalDateTime createdAt, User user) {

        this.question = question;
        this.answer = answer;
        this.responseStatus = responseStatus;
        this.createdAt = createdAt;
        this.user = user;
    }
}
