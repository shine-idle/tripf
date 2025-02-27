package com.shineidle.tripf.global.common.util.redis;

import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.LockErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RedisLockAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(redisLock)")
    public Object handleRedisLock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String key = parseKey(redisLock.key(), joinPoint.getArgs());
        RLock lock = redissonClient.getLock(key);

        boolean acquired = false;
        try {
            acquired = lock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), TimeUnit.SECONDS);
            if (!acquired) {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String parseKey(String keyTemplate, Object[] args) {
        for (Object arg : args) {
            if (arg instanceof User) {
                keyTemplate = keyTemplate.replace("{userId}", String.valueOf(((User) arg).getId()));
            }
        }
        return keyTemplate;
    }
}
