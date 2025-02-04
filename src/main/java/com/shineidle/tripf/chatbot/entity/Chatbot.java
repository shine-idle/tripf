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

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    protected Chatbot() {}

    public Chatbot(String question, String answer, ResponseStatus responseStatus, LocalDateTime createdAt, User user) {
        this.question = question;
        this.answer = answer;
        this.responseStatus = responseStatus;
        this.createdAt = createdAt;
        this.user = user;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}