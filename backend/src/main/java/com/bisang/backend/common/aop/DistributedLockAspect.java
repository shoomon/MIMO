package com.bisang.backend.common.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.bisang.backend.common.annotation.DistributedLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private static final int MAX_RETRY_COUNT = 3;

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock) && args(key, ..)")
    public Object lock(
            ProceedingJoinPoint joinPoint,
            String key,
            DistributedLock distributedLock
    ) throws Throwable {
        String name = distributedLock.name();
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        RLock lock = redissonClient.getLock(name + ":" + key);
        log.info("{} 락 획득 시도 (Key: {})", name, key);
        boolean locked = acquireLockWithRetry(lock, waitTime, leaseTime);

        if (!locked) {
            log.warn("{} 락 획득 실패 (Key: {})", name, key);
            throw new IllegalStateException("최대 재시도 횟수를 초과하여 락을 획득할 수 없습니다.");
        }
        log.info("{} 락 획득 성공 (Key: {})", name, key);

        try {
            return joinPoint.proceed();
        } finally {
            releaseLock(lock);
            log.info("{} 락 해제 완료 (Key: {})", name, key);
        }
    }

    /**
     * 락 획득 시도
     */
    private boolean acquireLockWithRetry(RLock lock, long waitTime, long leaseTime) {
        for (int retry = 1; retry <= MAX_RETRY_COUNT; retry++) {
            if (tryLock(lock, waitTime, leaseTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 락 획득 시도
     */
    private boolean tryLock(RLock lock, long waitTime, long leaseTime) {
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("락 획득 중 인터럽트 발생: {}", e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 락 해제 로직
     */
    private void releaseLock(RLock lock) {
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (IllegalMonitorStateException e) {
            log.error("이미 해제된 락을 해제하려 시도함: {}", e.getMessage());
        } catch (Exception e) {
            log.error("락 해제 중 예외 발생: {}", e.getMessage());
        }
    }
}
