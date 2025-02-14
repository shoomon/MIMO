package com.bisang.backend.alarm.repository;

import com.bisang.backend.alarm.controller.AlarmDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.bisang.backend.schedule.domain.QScheduleParticipants.scheduleParticipants;
import static com.bisang.backend.schedule.domain.QTeamSchedule.teamSchedule;

@Repository
@RequiredArgsConstructor
public class AlarmQuerydslRepository {
    private final JPAQueryFactory queryFactory;


    public List<AlarmDto> getAlarms(LocalDateTime time) {
        LocalDateTime endTime = time.plusMinutes(1);

        queryFactory
            .select(Projections.constructor,
                )
            .from(teamSchedule)
            .join(scheduleParticipants)
            .on(teamSchedule.id.eq(scheduleParticipants.teamScheduleId))
            .where(teamSchedule.date.eq(time), teamSchedule)
    }
}
