package com.bisang.backend.common.aspect;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.common.service.RequestLimitRedisService;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class S3LimitAspect {
    private final RequestLimitRedisService requestLimitRedisService;

    /**
     * @S3LimitAspect 메서드 레벨 적용, S3Upload에 관해 userId에 따라 접근 횟수를 제한함. 1분에 5회
     */
    @Around("@annotation(com.bisang.backend.common.annotation.S3Limiter) && args(userId, ..)")
    public Object validateTeamLeader(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        requestLimitRedisService.IsRequestPossible("S3Upload", String.valueOf(userId));
        return joinPoint.proceed();
    }
}
