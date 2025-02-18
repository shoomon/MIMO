package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;
import static com.bisang.backend.team.domain.TeamUserRole.LEADER;

import java.util.Optional;

import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.repository.TeamJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.controller.response.TeamScheduleSpecificResponse;
import com.bisang.backend.schedule.controller.response.TeamSchedulesResponse;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleQuerydslRepository;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleEveryOneService {
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final ScheduleParticipantsJpaRepository participantsJpaRepository;

    @EveryOne
    @Transactional(readOnly = true)
    public TeamSchedulesResponse getSchedules(Long userId, Long teamId, ScheduleStatus status, Long teamScheduleId) {
        privateGuestValidation(userId, teamId);

        var schedules = teamScheduleQuerydslRepository.getTeamSimpleSchedules(teamId, status, teamScheduleId);
        Boolean hasNext = schedules.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : schedules.size();
        Long lastTeamScheduleId = size > 0 ? schedules.get(size - 1).teamScheduleId() : null;

        if (hasNext) {
            schedules = schedules.stream()
                .limit(size)
                .toList();
        }

        return new TeamSchedulesResponse(size, hasNext, lastTeamScheduleId, schedules);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamScheduleSpecificResponse getSpecificSchedule(Long userId, Long teamId, Long teamScheduleId) {
        privateGuestValidation(userId, teamId);

        var specific = teamScheduleQuerydslRepository.getTeamScheduleSpecific(teamScheduleId);
        var comments = teamScheduleQuerydslRepository.getTeamScheduleComments(userId, teamScheduleId);
        var profiles = teamScheduleQuerydslRepository.getProfilesByScheduleId(teamScheduleId);
        boolean isTeamMember = userId == null ? false :  isTeamMember(userId, teamId);
        boolean isTeamScheduleMember
                = participantsJpaRepository.existsByTeamScheduleIdAndUserId(teamScheduleId, userId);

        Boolean isMyTeamSchedule = false;
        Optional<TeamUser> teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId);
        if (teamUser.isPresent()) {
            if (teamUser.get().getRole().equals(LEADER)) {
                isMyTeamSchedule = true;
            }
        }
        return TeamScheduleSpecificResponse.builder()
                .teamScheduleId(teamScheduleId)
                .isTeamMember(isTeamMember)
                .isTeamScheduleMember(isTeamScheduleMember)
                .isMyTeamSchedule(isMyTeamSchedule)
                .status(specific.status())
                .location(specific.location())
                .date(specific.date())
                .price(specific.price())
                .nameOfLeader(specific.nameOfLeader())
                .profileUris(profiles)
                .maxParticipants(specific.maxParticipants())
                .currentParticipants(specific.currentParticipants())
                .title(specific.title())
                .description(specific.description())
                .comments(comments).build();
    }

    private void privateGuestValidation(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        if (team.getPrivateStatus().equals(PRIVATE)) {
            if (userId == null) {
                throw new ScheduleException(INVALID_REQUEST);
            }
            findTeamUserByTeamIdAndUserId(teamId, userId);
        }
    }

    private void findTeamUserByTeamIdAndUserId(Long teamId, Long userId) {
        teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
            .orElseThrow(() -> new ScheduleException(INVALID_REQUEST));
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
            .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    private boolean isTeamMember(Long userId, Long teamId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId).isPresent();
    }

    private TeamSchedule findTeamScheduleById(Long teamScheduleId) {
        return teamScheduleJpaRepository.findById(teamScheduleId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
