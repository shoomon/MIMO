package com.bisang.backend.alarm.repository;

import com.bisang.backend.alarm.controller.AlarmDto;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;
import static com.bisang.backend.user.domain.QUser.user;

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

//    public List<AlarmDto> getAlarms(Long teamScheduleId) {
//
//        queryFactory
//            .select(Projections.constructor(AlarmDto.class,
//                ))
//            .from(scheduleParticipants)
//            .join(user)
//            .on(scheduleParticipants.userId.eq(user.id))
//            .where(scheduleParticipants.teamScheduleId.eq(teamScheduleId))
//            .fetch();
//    }
}
