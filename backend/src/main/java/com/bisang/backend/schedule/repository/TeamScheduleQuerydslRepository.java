package com.bisang.backend.schedule.repository;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;

import java.util.List;

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
                        Expressions.constant(0L),
                        teamSchedule.date,
                        teamSchedule.maxParticipants,
                        teamSchedule.currentParticipants))
                .from(teamSchedule)
                .where(teamSchedule.id.eq(teamScheduleId))
                .fetchOne();
    }

    public TeamSimpleScheduleDto getTeamSimpleSchedules(Long teamId, ScheduleStatus status, Long teamScheduleId) {
        BooleanBuilder dynamicTeamScheduleIdLt = new BooleanBuilder();
        if (teamScheduleId != null) {
            dynamicTeamScheduleIdLt.and(teamSchedule.id.lt(teamScheduleId));
        }

        List<TeamSimpleScheduleDto> tmpSimpleScheduleDto = queryFactory
                .select(Projections.fields(TeamSimpleScheduleDto.class,
                        teamSchedule.id,
                        teamSchedule.date,
                        teamSchedule.title,
                        Expressions.constant(0D),
                        Expressions.constant(null)))
                .from(teamSchedule)
                .where(teamSchedule.teamId.eq(teamId), teamSchedule.scheduleStatus.eq(status), dynamicTeamScheduleIdLt)
                .orderBy(teamSchedule.id.desc())
                .limit(SHORT_PAGE_SIZE).fetch();

        List<Long> simpleScheduleIds = tmpSimpleScheduleDto
                .stream()
                .map(TeamSimpleScheduleDto::teamScheduleId)
                .toList();

        // 되게 복잡해지네...
        return null;
    }
}
