package com.bisang.backend.schedule.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.schedule.domain.QTeamScheduleComment.teamScheduleComment;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.controller.dto.ProfileDto;
import com.bisang.backend.schedule.controller.dto.TeamScheduleCommentDto;
import com.bisang.backend.schedule.controller.dto.TeamSimpleScheduleDto;
import com.bisang.backend.schedule.controller.dto.TeamSpecificScheduleDto;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamScheduleQuerydslRepository {
    private final JPAQueryFactory queryFactory;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    public List<TeamSchedule> getTeamScheduleByStatusAndDateTime(ScheduleStatus status, LocalDateTime lastDateTime) {
        BooleanBuilder lastDateTimeLt = new BooleanBuilder();
        if (lastDateTime != null) {
            lastDateTimeLt.and(teamSchedule.date.lt(lastDateTime));
        }

        return queryFactory
            .selectFrom(teamSchedule)
            .where(teamSchedule.scheduleStatus.eq(status), lastDateTimeLt)
            .orderBy(teamSchedule.id.desc())
            .limit(SMALL_SCHEDULE_PAGE_SIZE + 1)
            .fetch();
    }

    public List<TeamSchedule> getTeamScheduleByStatusAndScheduleIdLt(ScheduleStatus status, Long lastScheduleId) {
        BooleanBuilder lastScheduleIdLt = new BooleanBuilder();
        if (lastScheduleId != null) {
            lastScheduleIdLt.and(teamSchedule.id.lt(lastScheduleId));
        }

        return queryFactory
            .selectFrom(teamSchedule)
            .where(teamSchedule.scheduleStatus.eq(status), lastScheduleIdLt)
            .orderBy(teamSchedule.id.desc())
            .limit(SMALL_SCHEDULE_PAGE_SIZE + 1)
            .fetch();
    }


    public TeamSpecificScheduleDto getTeamScheduleSpecific(Long teamScheduleId) {
        TeamSchedule schedule = findScheduleById(teamScheduleId);
        TeamUser teamUser = findTeamUserById(schedule.getTeamUserId());

        return queryFactory
                .select(Projections.constructor(TeamSpecificScheduleDto.class,
                        teamSchedule.id,
                        teamSchedule.teamUserId,
                        teamSchedule.scheduleStatus,
                        Expressions.constant(teamUser.getNickname()),
                        teamSchedule.title,
                        teamSchedule.description,
                        teamSchedule.location,
                        teamSchedule.price,
                        teamSchedule.date,
                        teamSchedule.maxParticipants,
                        teamSchedule.currentParticipants))
                .from(teamSchedule)
                .where(teamSchedule.id.eq(teamScheduleId))
                .fetchOne();
    }

    public List<TeamScheduleCommentDto> getTeamScheduleComments(Long userId, Long teamScheduleId) {
        userId = userId == null ? 0L : userId;
        List<TeamScheduleCommentDto> comments = queryFactory
                .select(Projections.constructor(TeamScheduleCommentDto.class,
                        teamScheduleComment.id,
                        teamScheduleComment.userId.eq(userId),
                        user.profileUri,
                        Expressions.stringTemplate("CAST({0} AS string)", teamScheduleComment.teamUserId),
                        teamScheduleComment.createdAt,
                        teamScheduleComment.parentCommentId.coalesce(teamScheduleComment.id),
                        Expressions.booleanTemplate("CASE WHEN {0} IS NULL THEN FALSE ELSE TRUE END",
                                teamScheduleComment.parentCommentId).as("hasParent"),
                        teamScheduleComment.content))
                .from(teamScheduleComment).join(user).on(teamScheduleComment.userId.eq(user.id))
                .where(teamScheduleComment.teamScheduleId.eq(teamScheduleId))
                .fetch();

        List<Long> teamUserIds = comments.stream()
                .map(TeamScheduleCommentDto::name)
                .map(Long::valueOf)
                .distinct()
                .toList();

        Map<Long, String> nicknameMap = queryFactory
                .select(teamUser.id, teamUser.nickname)
                .from(teamUser)
                .where(teamUser.id.in(teamUserIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(teamUser.id),  // 키: teamUser.id
                        tuple -> tuple.get(teamUser.nickname) // 값: teamUser.nickname
                ));

        return comments.stream()
                .map(comment -> {
                    System.out.println(comment.name());
                    System.out.println(nicknameMap);
                    return new TeamScheduleCommentDto(
                            comment.teamScheduleCommentId(),
                            comment.isMyComment(),
                            comment.profileUri(),
                            nicknameMap.get(Long.valueOf(comment.name())),
                            comment.time(),
                            comment.commentSortId(),
                            comment.hasParent(),
                            comment.content()
                    );
                }).toList();
    }

    public List<ProfileDto> getProfilesByScheduleId(Long teamScheduleId) {
        return queryFactory
                .select(Projections.constructor(ProfileDto.class,
                                user.id,
                                user.profileUri))
                .from(scheduleParticipants)
                .join(user)
                .on(scheduleParticipants.userId.eq(user.id))
                .where(scheduleParticipants.teamScheduleId.eq(teamScheduleId))
                .fetch();
    }

    public List<TeamSimpleScheduleDto> getTeamSimpleSchedules(
            Long teamId,
            ScheduleStatus status,
            Long teamScheduleId
    ) {
        BooleanBuilder dynamicTeamScheduleIdLt = new BooleanBuilder();
        if (teamScheduleId != null) {
            dynamicTeamScheduleIdLt.and(teamSchedule.id.lt(teamScheduleId));
        }

        List<TeamSimpleScheduleDto> tmpSimpleScheduleDto = queryFactory
                .select(Projections.constructor(TeamSimpleScheduleDto.class,
                        teamSchedule.id,
                        teamSchedule.date,
                        teamSchedule.title,
                        teamSchedule.price,
                        Expressions.constant(Collections.emptyList())))
                .from(teamSchedule)
                .where(teamSchedule.teamId.eq(teamId), teamSchedule.scheduleStatus.eq(status), dynamicTeamScheduleIdLt)
                .orderBy(teamSchedule.id.desc())
                .limit(SHORT_PAGE_SIZE + 1).fetch();

        return tmpSimpleScheduleDto.stream()
                .map(simpleSchedule -> new TeamSimpleScheduleDto(
                        simpleSchedule.teamScheduleId(),
                        simpleSchedule.date(),
                        simpleSchedule.title(),
                        simpleSchedule.price(),
                        getTeamScheduleParticipants(simpleSchedule)
                )).toList();
    }

    private List<ProfileDto> getTeamScheduleParticipants(TeamSimpleScheduleDto simpleSchedule) {
        return queryFactory
                .select(Projections.constructor(ProfileDto.class,
                        user.id,
                        user.profileUri))
                .from(scheduleParticipants)
                .join(user)
                .on(scheduleParticipants.userId.eq(user.id))
                .where(scheduleParticipants.teamScheduleId.eq(simpleSchedule.teamScheduleId()))
                .fetch();
    }

    private TeamUser findTeamUserById(Long teamUserId) {
        return teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    private TeamSchedule findScheduleById(Long teamScheduleId) {
        return teamScheduleJpaRepository.findById(teamScheduleId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
