package com.bisang.backend.team.aspect;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class TeamRoleValidationAspect {
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    /**
     * @TeamLeader 메서드 검증 로직, team의 LeaderId와 요청자의 userId가 동일한 지 확인
     */
    @Around("@annotation(com.bisang.backend.team.annotation.TeamLeader) && args(userId, teamId, ..)")
    public Object validateTeamLeader(ProceedingJoinPoint joinPoint, Long userId, Long teamId) throws Throwable {
        Team team = teamJpaRepository.findById(teamId)
                                        .orElseThrow(() -> new TeamException(NOT_FOUND));

        if (!team.getTeamLeaderId().equals(userId)) {
            throw new TeamException(INVALID_REQUEST);
        }

        return joinPoint.proceed();
    }

    /**
     * @TeamCoLeader 메서드 검증 로직, teamUserId가 member가 아니라면 통과
     */
    @Around("@annotation(com.bisang.backend.team.annotation.TeamCoLeader) && args(userId, teamId, ..)")
    public Object validateTeamCoLeader(ProceedingJoinPoint joinPoint, Long userId, Long teamId) throws Throwable {
        TeamUser teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                                                    .orElseThrow(() -> new TeamException(NOT_FOUND));

        if (teamUser.isMember()) {
            throw new TeamException(INVALID_REQUEST);
        }

        return joinPoint.proceed();
    }

    /**
     * @TeamMember 메서드 검증 로직, 해당 UserId가 해당 Team에 존재하면 통과
     */
    @Around("@annotation(com.bisang.backend.team.annotation.TeamMember) && args(userId, teamId, ..)")
    public Object validateTeamMember(ProceedingJoinPoint joinPoint, Long userId, Long teamId) throws Throwable {
        teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));

        return joinPoint.proceed();
    }
}
