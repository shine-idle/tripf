package com.shineidle.tripf.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RedisDataInitializer implements CommandLineRunner {

    private final RedisService redisService;

    @Override
    public void run(String... args) throws Exception {

        redisService.saveQuestions("Q001", Arrays.asList("안녕하세요", "안녕!", "하이!", " 반가워", " 어이!", "안뇽", "ㅎㅇ", "뭐해?"));
        redisService.saveQuestions("Q002", Arrays.asList("결제가 안돼요", "결제 문제", "결제 오류", "결제 불가", "결제 안됨", "결제가 안됩니다"));
        redisService.saveQuestions("Q003", Arrays.asList("문제가 해결되지 않습니다", "해결되지않음", "문제해결"));
        redisService.saveQuestions("Q004", Arrays.asList("고마워", "감사합니다", "고맙습니다", "ㄱㅅ"));

        redisService.saveAnswers("Q001", "안녕하세요! 무엇을 도와드릴까요?");
        redisService.saveAnswers("Q002", "결제가 진행되지 않을 경우, 사용 중인 결제 수단의 상태를 확인하세요. 문제가 지속되면 고객 서비스 센터에 문의해 주세요.");
        redisService.saveAnswers("Q003", "문제가 해결되지 않았으면 고객 서비스 센터에 문의해 주세요. 구체적인 문제를 자세히 설명해 주시면 친절하게 도와드리겠습니다.");
        redisService.saveAnswers("Q004", "별말씀을요");

    }
}
