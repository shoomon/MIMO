package com.bisang.backend.schedule.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.schedule.domain.QTeamScheduleComment.teamScheduleComment;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bisang.backend.common.exception.ScheduleException;
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

@Service
@RequiredArgsConstructor
public class    TeamScheduleQuerydslRepository {
    private final JPAQueryFactory queryFactory;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

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

    public List<TeamScheduleCommentDto> getTeamScheduleComments(Long teamScheduleId) {
        List<TeamScheduleCommentDto> comments = queryFactory
                .select(Projections.constructor(TeamScheduleCommentDto.class,
                        teamScheduleComment.id,
                        user.profileUri,
                        user.nickname,
                        teamScheduleComment.createdAt,
                        teamScheduleComment.parentCommentId.coalesce(teamScheduleComment.id),
                        Expressions.booleanTemplate("CASE WHEN {0} IS NULL THEN FALSE ELSE TRUE END",
                                teamScheduleComment.parentCommentId).as("hasParent"),
                        teamScheduleComment.content))
                .from(teamScheduleComment).join(user).on(teamScheduleComment.userId.eq(user.id))
                .where(teamScheduleComment.teamScheduleId.eq(teamScheduleId))
                .fetch();

        List<String> nickname = comments.stream()
                .map(TeamScheduleCommentDto::name)
                .toList();

        Map<String, String> nicknameMap = queryFactory
                .select(user.nickname, teamUser.nickname)
                .from(user).join(teamUser).on(user.id.eq(teamUser.userId))
                .where(user.nickname.in(nickname))
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(user.nickname),  // 키: user.nickname
                        tuple -> tuple.get(teamUser.nickname) // 값: teamUser.nickname
                ));

        return comments.stream()
                .map(comment -> {
                    return new TeamScheduleCommentDto(
                            comment.teamScheduleCommentId(),
                            comment.profileUri(),
                            nicknameMap.get(comment.name()),
                            comment.time(),
                            comment.commentSortId(),
                            comment.hasParent(),
                            comment.content()
                    );
                }).toList();
    }

    public List<String> getProfilesByScheduleId(Long teamScheduleId) {
        return queryFactory
                .select(user.profileUri)
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

    private List<String> getTeamScheduleParticipants(TeamSimpleScheduleDto simpleSchedule) {
        return queryFactory
                .select(user.profileUri)
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
