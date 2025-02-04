package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.invite.domain.TeamInvite.createInviteRequest;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PUBLIC;
import static com.bisang.backend.team.domain.TeamUser.createTeamMember;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.invite.repository.TeamInviteJpaRepository;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamInviteJpaRepository teamInviteJpaRepository;

    @EveryOne
    @Transactional
    public void joinTeam(Long userId, Long teamId, String nickname, TeamNotificationStatus status) {
        isAlreadyJoinChecker(teamId, userId);

        Team team = findTeamById(teamId);
        Long currentUserCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        if (team.getMaxCapacity() < currentUserCount && team.getPrivateStatus() == PUBLIC) {
            TeamUser newTeamMember = createTeamMember(userId, teamId, nickname, status);
            teamUserJpaRepository.save(newTeamMember);
        }
    }

    @EveryOne
    @Transactional
    public void inviteRequest(Long userId, Long teamId, String memo) {
        isAlreadyJoinChecker(teamId, userId);
        isAlreadyRequestedChecker(teamId, userId);

        Team team = findTeamById(teamId);
        Long currentUserCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        if (team.getMaxCapacity() < currentUserCount && team.getPrivateStatus() == PRIVATE) {
            TeamInvite inviteRequest = createInviteRequest(teamId, userId, memo);
            teamInviteJpaRepository.save(inviteRequest);
        }
    }

    @Transactional
    public void updateNickname(Long userId, Long teamId, String nickname) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        teamUser.updateNickname(nickname);
        teamUserJpaRepository.save(teamUser);
    }

    /**
     * 글은 남아있는데 탈퇴한 회원은 어떻게 하지?
     */
    @Transactional
    public void deleteUser(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        teamUserJpaRepository.delete(teamUser);
    }

    private void isAlreadyJoinChecker(Long teamId, Long userId) {
        var optionalTeamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId);
        if (optionalTeamUser.isPresent()) {
            throw new TeamException(ALREADY_JOINED_MEMBER);
        }
    }

    private void isAlreadyRequestedChecker(Long teamId, Long userId) {
        var optionalTeamInvite = teamInviteJpaRepository.findByTeamIdAndUserId(teamId, userId);
        if (optionalTeamInvite.isPresent()) {
            throw new TeamException(ALREADY_REQUEST_MEMBER);
        }
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findTeamById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND_TEAM));
    }

    private TeamUser findTeamUserByTeamIdAndUserId(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(NOT_FOUND_TEAM));
    }
}
