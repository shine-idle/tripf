package com.shineidle.tripf.global.common.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에 데이터를 저장
     * @param key Redis 키
     * @param value 저장할 값
     * @param ttl 만료 시간 (Duration)
     */
    public void saveToRedis(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * Redis에서 데이터를 조회
     * @param key Redis 키
     * @return 조회한 값 (없으면 null)
     */
    public Object getFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Redis에서 데이터를 조회하고 없으면 DB에서 가져옴
     * @param key Redis 키
     * @param dbFetcher DB에서 데이터를 조회하는 메서드
     * @param ttl 만료 시간 (Duration)
     * @return Redis 또는 DB에서 가져온 값
     */
    public <T> T getOrFetchFromDB(String key, DbFetcher<T> dbFetcher, Duration ttl) {
        T value = (T) redisTemplate.opsForValue().get(key);

        if (value == null) {
            value = dbFetcher.fetch();
            saveToRedis(key, value, ttl);
        }

        return value;
    }

    /**
     *  레디스 조회 시 수행할 메서드
     */
    public interface DbFetcher<T> {
        T fetch();
    }
}
