package com.shineidle.tripf.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RedisDataInitializer implements CommandLineRunner {

    private final RedisChatbotService redisChatbotService;

    @Override
    public void run(String... args) throws Exception {

        redisChatbotService.saveQuestions("Q001", Arrays.asList("안녕하세요", "안녕!", "안녕~", "안녕", "안녕?", "안녕하세요. 도움이 필요해요.", "안녕하세요. 도와주세요."));
        redisChatbotService.saveQuestions("Q002", Arrays.asList("결제가 안돼요", "결제 문제", "결제 오류", "결제 불가", "결제 안됨", "결제가 안됩니다"));
        redisChatbotService.saveQuestions("Q003", Arrays.asList("해결할 수 없습니다", "해결되지 않아요", "문제 해결 안됨", "문제 해결", "문제 발생", "문제를 해결하고 싶어요"));
        redisChatbotService.saveQuestions("Q004", Arrays.asList("감사합니다", "감사", "정말 감사해요", "정말 감사합니다", "감사해요. 고생했어요", "감사해요. 수고했어요"));
        redisChatbotService.saveQuestions("UNKNOWN", Arrays.asList("skjfalksjdflajdl", "미ㅏ넝리ㅏㅁㄴ;ㅣㅇ러;ㅣㅁ나ㅓ리", "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ", "ㅣㅏㅓㅣㅏㅓ;ㅣㅏㅓㅣㅏㅓ", "asdfasdfasfasf", "lkjlkjljlkj;lklkj"));

        redisChatbotService.saveAnswers("Q001", "안녕하세요! 무엇을 도와드릴까요?");
        redisChatbotService.saveAnswers("Q002", "결제가 진행되지 않을 경우, 사용 중인 결제 수단의 상태를 확인하세요. 문제가 지속되면 고객 서비스 센터에 문의해 주세요.");
        redisChatbotService.saveAnswers("Q003", "문제가 해결되지 않았으면 고객 서비스 센터에 문의해 주세요. 구체적인 문제를 자세히 설명해 주시면 친절하게 도와드리겠습니다.");
        redisChatbotService.saveAnswers("Q004", "감사합니다. Tripf 사용 중 궁금한 것이 생기면 다음에 또 찾아주세요^^");
        redisChatbotService.saveAnswers("UNKNOWN", "알아듣지 못했어요. 정확하게 입력해주세요.");
    }
}
