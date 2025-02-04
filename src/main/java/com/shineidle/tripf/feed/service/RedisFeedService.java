package com.shineidle.tripf.feed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shineidle.tripf.feed.dto.FeedResponseDto;
import com.shineidle.tripf.feed.dto.HomeResponseDto;
import com.shineidle.tripf.feed.dto.MyFeedResponseDto;
import com.shineidle.tripf.feed.dto.RegionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisFeedService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * FeedResponseDto를 JSON으로 변환하여 Redis에 저장
     *
     * @param feedId          피드 식별자
     * @param feedResponseDto 저장할 피드 데이터
     */
    public void saveFeed(Long feedId, FeedResponseDto feedResponseDto) {
        String cacheKey = "feed:" + feedId;

        try {
            String jsonValue = objectMapper.writeValueAsString(feedResponseDto);
            redisTemplate.opsForValue().set(cacheKey, jsonValue);
            log.info("FeedResponseDto 캐싱 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("FeedResponseDto JSON 변환 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Redis에서 FeedResponseDto JSON을 가져와 객체로 변환
     *
     * @param feedId 피드 식별자
     * @return FeedResponseDto 객체 또는 null
     */
    public FeedResponseDto getFeed(Long feedId) {
        String cacheKey = "feed:" + feedId;
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData instanceof String cachedJson) {
            try {
                return objectMapper.readValue(cachedJson, FeedResponseDto.class);
            } catch (JsonProcessingException e) {
                log.error("FeedResponseDto JSON 역직렬화 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Redis 캐시에서 FeedResponseDto를 찾을 수 없음: {}", cacheKey);
        }
        return null;
    }

    /**
     * Redis에서 특정 키의 캐시 삭제
     *
     * @param key 삭제할 캐시 키
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
        log.info("Redis 캐시 삭제 완료: {}", key);
    }

    /**
     * Redis에서 피드 캐시 업데이트
     *
     * @param feedId          피드 식별자
     * @param feedResponseDto 갱신할 피드 데이터
     */
    public void updateCache(Long feedId, FeedResponseDto feedResponseDto) {
        String cacheKey = "feed:" + feedId;
        try {
            String jsonValue = objectMapper.writeValueAsString(feedResponseDto);
            redisTemplate.opsForValue().set(cacheKey, jsonValue);
            log.info("FeedResponseDto 캐싱 갱신 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("FeedResponseDto JSON 변환 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Redis에서 지역별 피드 데이터를 조회
     *
     * @param cacheKey 캐시 키
     * @return 지역 피드 데이터 목록 또는 null
     */
    public List<RegionResponseDto> getRegionCache(String cacheKey) {
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData instanceof String cachedJson) {
            try {
                return objectMapper.readValue(cachedJson, new TypeReference<List<RegionResponseDto>>() {});
            } catch (JsonProcessingException e) {
                log.error("RegionResponseDto JSON 역직렬화 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Redis 캐시에서 RegionResponseDto를 찾을 수 없음: {}", cacheKey);
        }
        return null;
    }

    /**
     * Redis에서 지역 조회 데이터를 저장
     *
     * @param cacheKey     캐시 키
     * @param regionFeeds  저장할 지역 피드 데이터 목록
     */
    public void saveRegionCache(String cacheKey, List<RegionResponseDto> regionFeeds) {
        try {
            String jsonValue = objectMapper.writeValueAsString(regionFeeds);
            redisTemplate.opsForValue().set(cacheKey, jsonValue, 30, TimeUnit.MINUTES);
            log.info("RegionResponseDto 캐싱 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("RegionResponseDto JSON 변환 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Redis에서 홈 화면 데이터를 조회
     *
     * @param cacheKey 캐시 키
     * @return HomeResponseDto 객체 또는 null
     */
    public HomeResponseDto getHomeCache(String cacheKey) {
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData instanceof String cachedJson) {
            try {
                return objectMapper.readValue(cachedJson, HomeResponseDto.class);
            } catch (JsonProcessingException e) {
                log.error("HomeResponseDto JSON 역직렬화 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Redis 캐시에서 HomeResponseDto를 찾을 수 없음: {}", cacheKey);
        }
        return null;
    }

    /**
     * Redis에 홈 화면 데이터를 저장
     *
     * @param cacheKey 캐시 키
     * @param homeData 저장할 홈 데이터
     */
    public void saveHomeCache(String cacheKey, HomeResponseDto homeData) {
        try {
            String jsonValue = objectMapper.writeValueAsString(homeData);
            redisTemplate.opsForValue().set(cacheKey, jsonValue, 30, TimeUnit.MINUTES);
            log.info("HomeResponseDto 캐싱 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("HomeResponseDto JSON 변환 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Redis에서 공용 홈 화면 데이터를 조회
     *
     * @param cacheKey 캐시 키
     * @return HomeResponseDto 객체 또는 null
     */
    public HomeResponseDto getPublicHomeCache(String cacheKey) {
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData instanceof String cachedJson) {
            try {
                return objectMapper.readValue(cachedJson, HomeResponseDto.class);
            } catch (JsonProcessingException e) {
                log.error("HomeResponseDto JSON 역직렬화 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Redis 캐시에서 Public Home 데이터를 찾을 수 없음: {}", cacheKey);
        }
        return null;
    }

    /**
     * Redis에 공용 홈 화면 데이터를 저장
     *
     * @param cacheKey 캐시 키
     * @param homeData 저장할 공용 홈 데이터
     */
    public void savePublicHomeCache(String cacheKey, HomeResponseDto homeData) {
        try {
            String jsonValue = objectMapper.writeValueAsString(homeData);
            redisTemplate.opsForValue().set(cacheKey, jsonValue, 30, TimeUnit.MINUTES);
            log.info("Public Home 데이터 캐싱 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("Public Home 데이터 JSON 변환 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Redis에서 사용자의 피드 목록을 조회
     *
     * @param cacheKey 캐시 키
     * @param pageable 페이지네이션 정보
     * @return 사용자의 피드 목록을 담은 Page 객체 또는 null
     */
    public Page<MyFeedResponseDto> getMyFeedsCache(String cacheKey, Pageable pageable) {
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);

        if (cachedData instanceof String cachedJson) {
            try {
                List<MyFeedResponseDto> myFeedsList = objectMapper.readValue(cachedJson, new TypeReference<List<MyFeedResponseDto>>() {});
                return new PageImpl<>(myFeedsList, pageable, myFeedsList.size());
            } catch (JsonProcessingException e) {
                log.error("MyFeedResponseDto JSON 역직렬화 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Redis 캐시에서 MyFeedResponseDto를 찾을 수 없음: {}", cacheKey);
        }
        return null;
    }

    /**
     * Redis에 사용자의 피드 목록을 저장
     *
     * @param cacheKey 캐시 키
     * @param myFeeds  저장할 피드 목록 (페이지네이션 적용)
     */
    public void saveMyFeedsCache(String cacheKey, Page<MyFeedResponseDto> myFeeds) {
        try {
            String jsonValue = objectMapper.writeValueAsString(myFeeds.getContent());
            redisTemplate.opsForValue().set(cacheKey, jsonValue, 30, TimeUnit.MINUTES);
            log.info("MyFeedResponseDto 캐싱 완료: {}", cacheKey);
        } catch (JsonProcessingException e) {
            log.error("MyFeedResponseDto JSON 변환 실패: {}", e.getMessage(), e);
        }
    }
}