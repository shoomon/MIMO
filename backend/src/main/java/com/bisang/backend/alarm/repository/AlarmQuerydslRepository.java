package com.bisang.backend.alarm.repository;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.dto.TempAlarmDto;
import com.bisang.backend.alarm.domain.Alarm;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.team.domain.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.bisang.backend.alarm.domain.QAlarm.alarm;
import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.team.domain.TeamNotificationStatus.ACTIVE;

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

        return queryFactory
            .select(Projections.constructor(TempAlarmDto.class,
                teamUser.userId,
                scheduleParticipants.teamScheduleId,
                Expressions.stringTemplate(scheduleTeam.getName() + " 모임의 일정입니다."),
                Expressions.stringTemplate(teamSchedule.getTitle() + ": 해당 일정을 확인해주세요")
                ))
            .from(scheduleParticipants)
            .join(teamUser)
            .on(scheduleParticipants.teamUserId.eq(teamUser.id))
            .where(scheduleParticipants.teamScheduleId.eq(teamScheduleId), teamUser.status.eq(ACTIVE))
            .fetch();
    }

    public List<AlarmDto> getUserAlarms(Long userId) {
        return queryFactory
            .select(Projections.constructor(AlarmDto.class,
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
