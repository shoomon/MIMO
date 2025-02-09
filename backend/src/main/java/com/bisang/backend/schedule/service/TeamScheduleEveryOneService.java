package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.controller.response.TeamSchedulesResponse;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleQuerydslRepository;
import com.bisang.backend.team.annotation.EveryOne;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleEveryOneService {
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @EveryOne
    @Transactional(readOnly = true)
    public TeamSchedulesResponse getSchedules(Long teamId, ScheduleStatus status, Long teamScheduleId) {
        var schedules = teamScheduleQuerydslRepository.getTeamSimpleSchedules(teamId, status, teamScheduleId);
        Boolean hasNext = schedules.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : schedules.size();
        Long lastTeamScheduleId = size > 0 ? schedules.get(size-1).teamScheduleId() : null;

        if (hasNext) {
            schedules.remove(size);
        }

        return new TeamSchedulesResponse(size, hasNext, lastTeamScheduleId, schedules);
    }

    private TeamSchedule findTeamScheduleById(Long teamScheduleId) {
        return teamScheduleJpaRepository.findById(teamScheduleId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
