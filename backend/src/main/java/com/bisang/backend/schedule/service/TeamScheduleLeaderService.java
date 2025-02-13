package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleLeaderService {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @TeamLeader
    @Transactional
    public TeamSchedule createTeamSchedule(
        Long userId,
        Long teamId,
        String title,
        String description,
        String location,
        LocalDateTime date,
        Long maxParticipants,
        Long price,
        ScheduleStatus status
    ) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(userId, teamId);
        TeamSchedule teamSchedule = TeamSchedule.builder()
                .teamId(teamId)
                .teamUserId(teamUser.getId())
                .title(title)
                .description(description)
                .location(location)
                .date(date)
                .maxParticipants(maxParticipants)
                .price(price)
                .status(status).build();
        teamScheduleJpaRepository.save(teamSchedule);

        ScheduleParticipants creator = ScheduleParticipants.creator(teamSchedule.getId(), userId, teamUser.getId());
        scheduleParticipantsJpaRepository.save(creator);

        return teamSchedule;
    }

    private TeamUser findTeamUserByTeamIdAndUserId(Long userId, Long teamId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    @TeamLeader
    @Transactional
    public void updateTeamSchedule(
            Long userId,
            Long teamId,
            Long teamScheduleId,
            String title,
            String description,
            String location,
            LocalDateTime date,
            Long maxParticipants,
            Long price,
            ScheduleStatus status
    ) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);

        teamSchedule.updateTitle(title);
        teamSchedule.updateDescription(description);
        teamSchedule.updateLocation(location);
        teamSchedule.updateDate(date);
        teamScheduleParticipantsValidation(teamScheduleId, maxParticipants);
        teamSchedule.updateMaxParticipants(maxParticipants);
        teamSchedule.updatePrice(price);
        teamSchedule.updateStatus(status);
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @TeamLeader
    @Transactional
    public void deleteTeamSchedule(Long userId, Long teamId, Long teamScheduleId) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        teamScheduleValidation(teamId, teamSchedule);
        teamScheduleJpaRepository.delete(teamSchedule);
    }

    private void teamScheduleValidation(Long teamId, TeamSchedule teamSchedule) {
        if (!teamSchedule.getTeamId().equals(teamId)) {
            throw new ScheduleException(INVALID_REQUEST);
        }
    }

    private void teamScheduleParticipantsValidation(Long teamScheduleId, Long maxParticipants) {
        if (scheduleParticipantsJpaRepository.countByTeamScheduleId(teamScheduleId) > maxParticipants) {
            throw new ScheduleException(FULL_SCHEDULE);
        }
    }

    private TeamSchedule findTeamScheduleById(Long teamScheduleId) {
        return teamScheduleJpaRepository.findById(teamScheduleId)
            .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
