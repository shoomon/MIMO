package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleParticipantsService {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @DistributedLock(name = "schedule")
    @Transactional
    public void joinSchedule(
            String key,
            Long userId,
            Long teamScheduleId
    ) {
        TeamSchedule teamSchedule = findScheduleById(teamScheduleId);

        TeamUser teamUser = getTeamUser(userId, teamSchedule);
        isJoinPossibleValidation(teamSchedule);

        var participants = ScheduleParticipants.participants(teamScheduleId, userId, teamUser.getId());
        scheduleParticipantsJpaRepository.save(participants);

        teamSchedule.increaseCurrentParticipants();
        teamScheduleJpaRepository.save(teamSchedule);
    }

    @DistributedLock(name = "schedule")
    @Transactional
    public void leaveSchedule(
            String key, // teamScheduleId의 String 형태
            Long userId,
            Long scheduleParticipantsId
    ) {
        ScheduleParticipants participants = findParticipantsById(scheduleParticipantsId);
        participantsValidation(userId, participants);
        scheduleParticipantsJpaRepository.delete(participants);

        TeamSchedule schedule = findScheduleById(participants.getTeamScheduleId());
        schedule.decreaseCurrentParticipants();
        teamScheduleJpaRepository.save(schedule);
    }

    private void participantsValidation(Long userId, ScheduleParticipants participants) {
        if (!participants.getUserId().equals(userId)) {
            throw new ScheduleException(INVALID_REQUEST);
        }
    }

    private void isJoinPossibleValidation(TeamSchedule teamSchedule) {
        if (teamSchedule.getCurrentParticipants() + 1 > teamSchedule.getMaxParticipants()) {
            throw new ScheduleException(FULL_SCHEDULE);
        }
    }

    private TeamUser getTeamUser(Long userId, TeamSchedule teamSchedule) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamSchedule.getTeamId(), userId)
                .orElseThrow(() -> new ScheduleException(INVALID_REQUEST));
    }

    private ScheduleParticipants findParticipantsById(Long scheduleParticipantsId) {
        return scheduleParticipantsJpaRepository.findById(scheduleParticipantsId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    private TeamSchedule findScheduleById(Long teamScheduleId) {
        return teamScheduleJpaRepository.findById(teamScheduleId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
