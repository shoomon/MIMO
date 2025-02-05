package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.invite.repository.TeamInviteJpaRepository;
import com.bisang.backend.team.annotation.TeamCoLeader;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.controller.dto.TeamInviteDto;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.team.repository.TeamUserQuerydslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamLeaderService {
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamUserQuerydslRepository teamUserQuerydslRepository;
    private final TeamInviteJpaRepository teamInviteJpaRepository;

    @TeamCoLeader
    @Transactional
    public void approveInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        coLeaderValidation(teamUser);

        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInvite.approveInvitation();
        teamInviteJpaRepository.save(teamInvite);
    }

    @TeamCoLeader
    @Transactional
    public void rejectInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        coLeaderValidation(teamUser);

        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInvite.rejectInvitation();
        teamInviteJpaRepository.save(teamInvite);
    }

    @TeamLeader
    @Transactional
    public void upgradeRole(Long userId, Long teamId, Long teamUserId) {
        Team team = findTeamById(teamId);
        leaderValidation(team, userId);

        TeamUser teamUser = findTeamUserById(teamUserId);
        leaderCantDownGradeValidation(teamUser, userId);

        teamUser.upgradeRole();
        teamUserJpaRepository.save(teamUser);
    }

    @TeamLeader
    @Transactional
    public void downgradeRole(Long userId, Long teamId, Long teamUserId) {
        Team team = findTeamById(teamId);
        leaderValidation(team, userId);

        TeamUser teamUser = findTeamUserById(teamUserId);
        leaderCantDownGradeValidation(teamUser, userId);

        teamUser.downgradeRole();
        teamUserJpaRepository.save(teamUser);
    }

    @TeamLeader
    @Transactional
    public void deleteUser(Long userId, Long teamId, Long teamUserId) {
        Team team = findTeamById(teamId);
        leaderValidation(team, userId);

        TeamUser teamUser = findTeamUserById(teamUserId);
        teamUserJpaRepository.delete(teamUser);
    }

    @TeamCoLeader
    @Transactional(readOnly = true)
    public List<TeamUserDto> findTeamUser(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        coLeaderValidation(teamUser);

        return teamUserQuerydslRepository.getTeamUserInfo(teamId);
    }

    @TeamCoLeader
    @Transactional(readOnly = true)
    public List<TeamInviteDto> findTeamInviteRequest(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        coLeaderValidation(teamUser);

        return teamUserQuerydslRepository.getTeamInviteInfo(teamId);
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findTeamById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamInvite findTeamInviteById(Long teamInviteId) {
        return teamInviteJpaRepository.findById(teamInviteId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamUser findTeamUserByTeamIdAndUserId(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamUser findTeamUserById(Long teamUserId) {
        return teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private void coLeaderValidation(TeamUser teamUser) {
        if (teamUser.isMember()) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

    private void leaderValidation(Team team, Long userId) {
        if (!team.getTeamLeaderId().equals(userId)) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

    private void leaderCantDownGradeValidation(TeamUser teamUser, Long userId) {
        if (teamUser.getUserId().equals(userId)) { // 방장이 자신의 등급을 낮추는 건 불가능
            throw new TeamException(INVALID_REQUEST);
        }
    }
}
