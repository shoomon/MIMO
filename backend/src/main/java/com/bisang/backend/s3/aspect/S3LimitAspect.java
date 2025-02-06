package com.bisang.backend.s3.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.bisang.backend.s3.service.RequestLimitRedisService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class S3LimitAspect {
    private final RequestLimitRedisService requestLimitRedisService;

    /**
     * @S3LimitAspect 메서드 레벨 적용, S3Upload에 관해 userId에 따라 접근 횟수를 제한함. 1분에 5회
     */
    @Around("@annotation(com.bisang.backend.s3.annotation.S3Limiter) && args(userId, ..)")
    public Object validateTeamLeader(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        requestLimitRedisService.isRequestPossible("S3Upload", String.valueOf(userId));
        return joinPoint.proceed();
    }
}
