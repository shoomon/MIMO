package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.controller.response.TeamSchedulesResponse;
import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleQuerydslRepository;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.annotation.TeamLeader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @TeamLeader
    @Transactional
    public TeamSchedule createTeamSchedule(
        Long userId,
        Long teamId,
        Long teamUserId,
        String title,
        String description,
        String location,
        LocalDateTime date,
        Long maxParticipants
    ) {
        TeamSchedule teamSchedule = TeamSchedule.builder()
            .teamId(teamId)
            .teamUserId(teamUserId)
            .title(title)
            .description(description)
            .location(location)
            .date(date)
            .maxParticipants(maxParticipants).build();
        teamScheduleJpaRepository.save(teamSchedule);


        ScheduleParticipants creator = ScheduleParticipants.creator(teamSchedule.getId(), userId, teamUserId);
        scheduleParticipantsJpaRepository.save(creator);

        return teamSchedule;
    }

    @TeamLeader
    @Transactional
    public void updateTitle(Long userId, Long teamId, Long teamScheduleId, String title) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updateTitle(title);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void updateDescription(Long userId, Long teamId, Long teamScheduleId, String description) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updateDescription(description);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void updateLocation(Long userId, Long teamId, Long teamScheduleId, String location) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updateLocation(location);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void updateDate(Long userId, Long teamId, Long teamScheduleId, LocalDateTime date) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updateDate(date);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void updateParticipants(Long userId, Long teamId, Long teamScheduleId, Long maxParticipants) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updateMaxParticipants(maxParticipants);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void updatePrice(Long userId, Long teamId, Long teamScheduleId, Long price) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamSchedule.updatePrice(price);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamSchedulesResponse getAdhocSchedule(Long teamId, ScheduleStatus status, Long teamScheduleId) {
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
