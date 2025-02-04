package com.shineidle.tripf.chatbot.service;

import com.shineidle.tripf.chatbot.RedisChatbotService;
import com.shineidle.tripf.chatbot.dto.ChatbotQuestionsResponseDto;
import com.shineidle.tripf.chatbot.dto.ChatbotRequestDto;
import com.shineidle.tripf.chatbot.dto.ChatbotResponseDto;
import com.shineidle.tripf.chatbot.entity.Chatbot;
import com.shineidle.tripf.chatbot.repository.ChatbotRepository;
import com.shineidle.tripf.chatbot.type.ResponseStatus;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.LockErrorCode;
import com.shineidle.tripf.common.util.RedisUtils;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.user.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final RedisUtils redisUtils;
    private final RedissonClient redissonClient;
    private final RedisChatbotService redisChatbotService;
    private DocumentCategorizerME categorizer;  // NLP 모델을 통한 분류기
    private final ChatbotRepository chatbotRepository;

    /**
     * 애플리케이션 시작 시 NLP 모델 초기화
     *
     * @throws Exception NLP 모델 파일 없는 경우
     */
    @PostConstruct
    public void init() throws Exception {
        try (InputStream modelIn = getClass().getResourceAsStream("/models/doccat.bin")) {
            if (modelIn == null) {
                throw new IllegalStateException("NLP 모델 파일을 찾을 수 없습니다: /models/doccat.bin");
            }
            DoccatModel model = new DoccatModel(modelIn);
            log.info("Model loaded successfully.");

            categorizer = new DocumentCategorizerME(model);
        }
    }

    /**
     * 사용자 질문 입력 및 챗봇 답변 생성
     *
     * @param chatbotRequestDto {@link ChatbotRequestDto} 챗봇 요청 Dto
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    @Override
    public ChatbotResponseDto createChatbotResponse(ChatbotRequestDto chatbotRequestDto) {

        Long userId = UserAuthorizationUtil.getLoginUserId();
        User loginedUser = UserAuthorizationUtil.getLoginUser();

        String lockKey = "chatbot:lock:user:" + userId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {

                // 질문 분석 및 분류
                String[] tokens = tokenizeKorean(chatbotRequestDto.getQuestion());
                log.info("Tokens: " + Arrays.toString(tokens));   // 토큰화된 질문 출력

                double[] outcomes = categorizer.categorize(tokens);
                log.info("Outcomes: " + Arrays.toString(outcomes));   // 분류 결과 출력

                String category = categorizer.getBestCategory(outcomes);
                log.info("Categorized Question: " + category);    // 분류된 카테고리 출력 (디버그용)

                double maxScore = Arrays.stream(outcomes).max().orElse(0.0);
                log.info("Max Score: " + maxScore); // 최대 확률 값 출력

                // 신뢰도 낮으면 UNKNOWN 처리
                boolean isUnknown = maxScore < 0.5;
                if (isUnknown) {
                    category = "UNKNOWN";
                }

                // Redis에서 해당 카테고리의 답변 조회
                String answer = redisChatbotService.getAnswer(category);
                if (answer == null) {
                    answer = "알아듣지 못했어요.";
                }
                log.info("Answer from Redis: " + answer);  // 답변 확인용 로그

                // 응답 상태 결정
                ResponseStatus responseStatus = isUnknown || "알아듣지 못했어요.".equals(answer)
                        ? ResponseStatus.FAILURE
                        : ResponseStatus.SUCCESS;

                // 대화 기록 저장
                Chatbot log = new Chatbot(
                        chatbotRequestDto.getQuestion(),
                        answer,
                        responseStatus,
                        LocalDateTime.now(),
                        loginedUser
                );

                Chatbot savedChatbot = chatbotRepository.save(log);

                // 생성된 대화 기록을 Redis에 저장
                String redisKey = "conversation_log:" + userId;
                List<ChatbotResponseDto> logs = findConversationLogs();
                logs.add(ChatbotResponseDto.toDto(savedChatbot));
                redisUtils.saveToRedis(redisKey, logs, Duration.ofHours(1));

                // 응답 반환
                return ChatbotResponseDto.toDto(savedChatbot);
            } else {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                ;
            }
        }
    }

    /**
     * 사용자 대화 기록 조회
     *
     * @return {@link ChatbotResponseDto} 챗봇 응답 Dto
     */
    @Override
    public List<ChatbotResponseDto> findConversationLogs() {

        Long loginUserId = UserAuthorizationUtil.getLoginUserId();

        String redisKey = "conversation_log:" + loginUserId;

        // Redis에서 대화 기록 조회, 없으면 DB에서 조회하고 Redis에 저장
        return redisUtils.getOrFetchFromDB(redisKey,
                () -> {
                    // DB에서 대화 기록 조회
                    List<Chatbot> logs = chatbotRepository.findByUserId(loginUserId);
                    List<ChatbotResponseDto> responseLogs = new ArrayList<>();

                    for (Chatbot log : logs) {
                        responseLogs.add(ChatbotResponseDto.toDto(log));
                    }
                    return responseLogs;
                },
                Duration.ofHours(1) // TTL 1시간
        );

    }

    /**
     * 질문 목록 조회
     *
     * @return {@link ChatbotQuestionsResponseDto} 챗봇 질문 목록 응답 Dto
     */
    @Override
    public List<ChatbotQuestionsResponseDto> findAllChatbotQuestion() {

        // Redis에서 모든 질문 조회
        Map<String, List<String>> allQuestions = redisChatbotService.getAllQuestions();

        return allQuestions.entrySet().stream()
                .map(entry -> new ChatbotQuestionsResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
   }

    /**
     * 한국어 텍스트를 분석하여 토큰화하는 메서드
     *
     * @param text 토큰화할 입력 문자열
     * @return 입력 문자열을 토큰화한 결과를 문자열 배열로 반환
     */
    private String[] tokenizeKorean (String text){
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
}
