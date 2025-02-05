package com.example.lock;

import com.bisang.backend.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private static final int MAX_RETRY_COUNT = 3;
    private static final long RETRY_DELAY_MS = 100;

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public void lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String name = distributedLock.name();
        String key = distributedLock.key();
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        log.info("{} 락에 대한 획득을 시도합니다.", name);

        RLock lock = redissonClient.getLock(key);
        boolean locked = acquireLockWithRetry(lock, waitTime, leaseTime);

        if (!locked) {
            log.info("{} 락에 대한 획득에 실패했습니다.", name);
            throw new IllegalStateException("최대 재시도 횟수를 초과하여 락을 획득할 수 없습니다.");
        }

        log.info("{} 락 획득 성공: {}", name, key);
        try {
            joinPoint.proceed();
        } finally {
            releaseLock(lock, key);
            log.info("{} 락 해제 완료: {}", name, key);
        }
    }

    /**
     * 락 획득 시도 (최대 3번까지 재시도)
     */
    private boolean acquireLockWithRetry(RLock lock, long waitTime, long leaseTime) {
        for (int retry = 1; retry <= MAX_RETRY_COUNT; retry++) {
            if (tryLock(lock, waitTime, leaseTime)) {
                return true;
            }
            threadSleep(retry);
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
            log.error("락 획득 중 인터럽트 발생 : {}", e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 재시도 대기
     */
    private void threadSleep(int retry) {
        log.warn("락 획득 실패, 재시도 중 ({}번째)", retry);
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            log.warn("재시도 대기 중 인터럽트 발생: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 락 해제 로직
     */
    private void releaseLock(RLock lock, String key) {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}