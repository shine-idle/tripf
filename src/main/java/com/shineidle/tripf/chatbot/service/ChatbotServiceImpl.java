package com.shineidle.tripf.chatbot.service;

import com.shineidle.tripf.chatbot.RedisService;
import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;
import com.shineidle.tripf.chatbot.entity.Chatbot;
import com.shineidle.tripf.chatbot.repository.ChatbotRepository;
import com.shineidle.tripf.chatbot.type.ResponseStatus;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.LockErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.user.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final RedissonClient redissonClient;
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;
    private DocumentCategorizerME categorizer;  // NLP 모델을 통한 분류기
    private final ChatbotRepository chatbotRepository;

//    @PersistenceContext
//    private final EntityManager entityManager;

    // 애플리케이션 시작 시 NLP 모델 초기화
    @PostConstruct
    public void init() throws Exception {
        try (InputStream modelIn = getClass().getResourceAsStream("/models/doccat.bin")) {
           if (modelIn == null) {
               throw new IllegalStateException("NLP 모델 파일을 찾을 수 없습니다: /models/doccat.bin");
           }
            DoccatModel model = new DoccatModel(modelIn);
            categorizer = new DocumentCategorizerME(model);
        }
    }

    // 사용자 질문 입력 및 챗봇 답변 생성
    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto) {

        User userId = UserAuthorizationUtil.getLoginUser();

        String lockKey = "chatbot:lock:user:" + userId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10,30, TimeUnit.SECONDS)) {

                // 질문 분석 및 분류
                String[] tokens = tokenizeKorean(chatbotRequestDto.getQuestion());
                double[] outcomes = categorizer.categorize(tokens);
                String category = categorizer.getBestCategory(outcomes);

                // Redis에서 해당 카테고리의 답변 조회
                String answer = redisService.getAnswer(category);

                // 대화 기록 저장
                Chatbot log = new Chatbot(
                        chatbotRequestDto.getQuestion(),
                        answer != null ? answer : "알아듣지 못했어요.",
                        answer != null ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE,
                        LocalDateTime.now(),
                        userId
                );

                Chatbot chatbot = chatbotRepository.save(log);

                // 응답 반환
                return ChatbotResponseDto.toDto(chatbot);
            } else {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();;
            }
        }
    }

    // 사용자 대화 기록 조회

    // 질문 목록 조회
    @Override
    public List<ChatbotQuestionsResponseDto> findAllChatbotQuestion() {

        checkRedisData();

        List<String> allQuestions = redisService.getAllQuestions();

        // 타입을 확인하는 로그 추가
        System.out.println("All Questions from Redis: " + allQuestions);
        for (Object question : allQuestions) {
            System.out.println("Question Type: " + question.getClass().getName());  // 타입 출력
        }
        return allQuestions.stream()
                .map(question -> new ChatbotQuestionsResponseDto(question))
                .collect(Collectors.toList());

//        return allQuestions.stream()
//                .filter(question -> question instanceof String)
//                .map(question -> new ChatbotQuestionsResponseDto((String) question))
//                .collect(Collectors.toList());

//        return allQuestions.stream()
//                .filter(question -> question instanceof String)
//                .map(question -> {
//                    try {
//                        return new ChatbotQuestionsResponseDto((String) question);
//                    } catch (ClassCastException e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
    }

    public void checkRedisData() {
        Set<String> keys = redisTemplate.keys("*:questions");
        if (keys != null) {
            for (String key : keys) {
                System.out.println("Key: " + key + ", Data: " + redisTemplate.opsForList().range(key, 0, -1));
            }
        }
    }

    // 한국어 텍스트를 분석하여 토큰화하는 메서드
    private String[] tokenizeKorean(String text) {
        List<String> tokens = new ArrayList<>();
        try (Analyzer analyzer = new KoreanAnalyzer();
             var tokenStream = analyzer.tokenStream(null, new StringReader(text))) {
            tokenStream.reset();
            CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                tokens.add(termAtt.toString());
            }
            tokenStream.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokens.toArray(new String[0]);
    }

    // 사용자 검증
    public User getAuthenticatedUser() {
        Long userId = UserAuthorizationUtil.getLoginUserId();
        return userId != null ? new User(userId) : null;
    }}
