package com.shineidle.tripf.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 질문을 카테고리별로 저장
    public void saveQuestions(String category, List<String> questions) {
        redisTemplate.delete(category + ":questions");
        redisTemplate.opsForList().rightPushAll(category + ":questions", questions);
    }

    // 답변을 카테고리별로 저장
    public void saveAnswers(String category, String answer) {
        redisTemplate.opsForValue().set(category + ":answer", answer);
    }

//    // 카테고리별 질문 조회
//    public List<Object> getQuestions(String category) {
//        return redisTemplate.opsForList().range(category + ":questions", 0, -1);
//    }

    // 모든 카테고리의 질문 목록 조회
    public Map<String, List<String>> getAllQuestions() {

        Set<String> keys = redisTemplate.keys("*:questions");
        System.out.println("Matching keys: " + keys);

        Map<String, List<String>> allQuestions = new HashMap<>();

        if (keys != null) {
            for (String key : keys) {

                // 카테고리 추출
                String category = key.split(":")[0];
                // Redis에서 질문 리스트 가져오기
                List<Object> rawQuestions = redisTemplate.opsForList().range(key, 0, -1);

                if (rawQuestions != null) {

                    // Object → String 변환
                    List<String> questions = rawQuestions.stream()
                            .filter(Objects::nonNull)  // null 방지
                            .map(Object::toString)
                            .toList();
                    allQuestions.put(category, questions);
                }
            }
        }
        return allQuestions;
    }

    // 카테고리별 답변 조회
    public String getAnswer(String category) {
        return (String) redisTemplate.opsForValue().get(category + ":answer");
    }
}
