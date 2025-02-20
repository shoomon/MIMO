package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.schedule.domain.ScheduleStatus.CLOSED;
import static com.bisang.backend.schedule.domain.ScheduleStatus.REGULAR;
import static com.bisang.backend.team.domain.TeamUserRole.LEADER;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDateTime;

import com.bisang.backend.team.annotation.TeamMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleLeaderService {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @TeamMember
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
        if (status.equals(CLOSED)) {
            throw new ScheduleException(INVALID_REQUEST);
        }

        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        if (!teamUser.getRole().equals(LEADER)) {
            if (status.equals(REGULAR)) {
                throw new ScheduleException(INVALID_REQUEST);
            }
        }

        TeamSchedule teamSchedule = TeamSchedule.builder()
                .teamId(teamId)
                .teamUserId(teamUser.getId())
                .title(title)
                .description(description)
                .location(location)
                .date(date.truncatedTo(MINUTES))
                .maxParticipants(maxParticipants)
                .price(price)
                .status(status).build();
        teamScheduleJpaRepository.save(teamSchedule);

        ScheduleParticipants creator = ScheduleParticipants.creator(teamSchedule.getId(), userId, teamUser.getId());
        scheduleParticipantsJpaRepository.save(creator);

        return teamSchedule;
    }

    @TeamMember
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
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        leaderCanManipulate(teamUser, teamSchedule);

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

    @TeamMember
    @Transactional
    public void deleteTeamSchedule(Long userId, Long teamId, Long teamScheduleId) {
        TeamSchedule teamSchedule = findTeamScheduleById(teamScheduleId);
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        teamScheduleValidation(teamId, teamSchedule);
        leaderCanManipulate(teamUser, teamSchedule);
        teamScheduleJpaRepository.delete(teamSchedule);
    }

    private TeamUser findTeamUserByTeamIdAndUserId(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
            .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    private static void leaderCanManipulate(TeamUser teamUser, TeamSchedule teamSchedule) {
        if (!teamUser.getRole().equals(LEADER)) {
            teamUserHaveSchedule(teamUser, teamSchedule);
        }
    }

    private static void teamUserHaveSchedule(TeamUser teamUser, TeamSchedule teamSchedule) {
        if (!teamUser.getId().equals(teamSchedule.getTeamUserId())) {
            throw new ScheduleException(INVALID_REQUEST);
        }
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
