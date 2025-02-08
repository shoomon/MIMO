package com.bisang.backend.schedule.repository;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bisang.backend.schedule.controller.dto.TeamSimpleScheduleDto;
import com.bisang.backend.schedule.controller.dto.TeamSpecificScheduleDto;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public TeamSpecificScheduleDto getTeamScheduleSpecific(Long teamScheduleId) {
        return queryFactory
                .select(Projections.fields(TeamSpecificScheduleDto.class,
                        teamSchedule.id,
                        teamSchedule.teamUserId,
                        Expressions.constant(0D),
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
                .select(Projections.fields(TeamSimpleScheduleDto.class,
                        teamSchedule.id,
                        teamSchedule.date,
                        teamSchedule.title,
                        Expressions.constant(0L),
                        Expressions.constant(null)))
                .from(teamSchedule)
                .where(teamSchedule.teamId.eq(teamId), teamSchedule.scheduleStatus.eq(status), dynamicTeamScheduleIdLt)
                .orderBy(teamSchedule.id.desc())
                .limit(SHORT_PAGE_SIZE + 1).fetch();

        List<Long> simpleScheduleIds = tmpSimpleScheduleDto
                .stream()
                .map(TeamSimpleScheduleDto::teamScheduleId)
                .toList();

        Map<Long, List<String>> scheduleProfileMap = queryFactory
                .from(scheduleParticipants)
                .join(user)
                .on(scheduleParticipants.userId.eq(user.id))
                .where(scheduleParticipants.teamScheduleId.in(simpleScheduleIds))
                .transform(groupBy(scheduleParticipants.teamScheduleId).as(list(user.profileUri)));

        return tmpSimpleScheduleDto.stream()
                .map(simpleSchedule -> new TeamSimpleScheduleDto(
                        simpleSchedule.teamScheduleId(),
                        simpleSchedule.date(),
                        simpleSchedule.title(),
                        0L,
                        scheduleProfileMap.get(simpleSchedule.teamScheduleId())
                )).toList();
    }
}
