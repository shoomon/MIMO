package com.bisang.backend.alarm.repository;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.dto.TempAlarmDto;
import com.bisang.backend.alarm.domain.Alarm;
import com.bisang.backend.common.exception.AlarmException;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.team.domain.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.bisang.backend.alarm.domain.QAlarm.alarm;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.team.domain.TeamNotificationStatus.ACTIVE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AlarmQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<TeamSchedule> getSchedules(LocalDateTime time, Long lastScheduleId) {
        BooleanBuilder scheduleIdLt = new BooleanBuilder();
        if (lastScheduleId != null) {
            scheduleIdLt.and(teamSchedule.id.lt(lastScheduleId));
        }

        return queryFactory
                .select(teamSchedule)
                .from(teamSchedule)
                .where(teamSchedule.date.eq(time), scheduleIdLt)
                .orderBy(teamSchedule.id.desc())
                .limit(SMALL_SCHEDULE_PAGE_SIZE)
                .fetch();
    }

    public List<TempAlarmDto> getAlarms(TeamSchedule teamSchedule) {
        Long teamScheduleId = teamSchedule.getId();

        Team scheduleTeam = queryFactory
            .selectFrom(team)
            .where(team.id.eq(teamSchedule.getTeamId()))
            .fetchOne();

        if (scheduleTeam == null) {
            throw new AlarmException(NOT_FOUND);
        }

        return queryFactory
                .select(Projections.constructor(TempAlarmDto.class,
                        teamUser.teamId,
                        teamUser.userId,
                        scheduleParticipants.teamScheduleId,
                        Expressions.constant(scheduleTeam.getName()),
                        Expressions.constant(teamSchedule.getTitle())
                ))
                .from(scheduleParticipants)
                .join(teamUser)
                .on(scheduleParticipants.teamUserId.eq(teamUser.id))
                .where(scheduleParticipants.teamScheduleId.eq(teamScheduleId), teamUser.status.eq(ACTIVE))
                .fetch();
    }

    public List<TempAlarmDto> getUserAlarms(Long userId) {
        return queryFactory
            .select(Projections.constructor(TempAlarmDto.class,
                alarm.id,
                alarm.userId,
                alarm.scheduleId,
                alarm.title,
                alarm.description
                ))
            .from(alarm)
            .where(alarm.userId.eq(userId))
            .fetch();
    }

    public Alarm getAlarm(Long alarmId) {
        return queryFactory
            .selectFrom(alarm)
            .where(alarm.id.eq(alarmId))
            .fetchOne();
    }
}
