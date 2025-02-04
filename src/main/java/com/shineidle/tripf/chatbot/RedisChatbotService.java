package com.shineidle.tripf.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisChatbotService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 질문을 카테고리별로 저장
     *
     * @param category 저장할 질문 카테고리
     * @param questions 저장할 질문 목록
     */
    public void saveQuestions(String category, List<String> questions) {
        redisTemplate.delete(category + ":questions");
        redisTemplate.opsForList().rightPushAll(category + ":questions", questions);
    }

    /**
     * 답변을 카테고리별로 저장
     *
     * @param category 저장할 답변 카테고리
     * @param answer 저장할 답변
     */
    public void saveAnswers(String category, String answer) {
        redisTemplate.opsForValue().set(category + ":answer", answer);
    }

    /**
     * 모든 카테고리의 질문 목록 조회
     *
     * @return 카테고리를 키로 하고 해당 카테고리에 속하는 질문 목록을 값으로 갖는 Map
     */
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

    /**
     * 카테고리별 답변 조회
     *
     * @param category 조회할 답변 카테고리
     * @return 해당 카테고리의 답변 문자열
     */
    public String getAnswer(String category) {
        return (String) redisTemplate.opsForValue().get(category + ":answer");
    }
}