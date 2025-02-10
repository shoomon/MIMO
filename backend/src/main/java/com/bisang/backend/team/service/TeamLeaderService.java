package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
import com.bisang.backend.team.controller.response.TeamUserResponse;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.domain.TeamUserRole;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.team.repository.TeamUserQuerydslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamLeaderService {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamUserQuerydslRepository teamUserQuerydslRepository;
    private final TeamInviteJpaRepository teamInviteJpaRepository;

    @TeamCoLeader
    @Transactional
    public void approveInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInvite.approveInvitation();
        teamInviteJpaRepository.save(teamInvite);
    }

    @TeamCoLeader
    @Transactional
    public void rejectInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInvite.rejectInvitation();
        teamInviteJpaRepository.save(teamInvite);
    }

    @TeamLeader
    @Transactional
    public void upgradeRole(Long userId, Long teamId, Long teamUserId) {
        TeamUser teamUser = findTeamUserById(teamUserId);
        leaderCantDownGradeValidation(teamUser, userId);

        teamUser.upgradeRole();
        teamUserJpaRepository.save(teamUser);
    }

    @TeamLeader
    @Transactional
    public void downgradeRole(Long userId, Long teamId, Long teamUserId) {
        TeamUser teamUser = findTeamUserById(teamUserId);
        leaderCantDownGradeValidation(teamUser, userId);

        teamUser.downgradeRole();
        teamUserJpaRepository.save(teamUser);
    }

    @TeamLeader
    @Transactional
    public void deleteUser(Long userId, Long teamId, Long teamUserId) {
        TeamUser teamUser = findTeamUserById(teamUserId);
        teamUserJpaRepository.delete(teamUser);
    }

    @TeamCoLeader
    @Transactional(readOnly = true)
    public TeamUserResponse findTeamUsers(Long userId, Long teamId, TeamUserRole role, Long teamUserId) {
        teamParameterValidation(role, teamUserId);

        List<TeamUserDto> teamUserInfos = teamUserQuerydslRepository.getTeamUserInfos(teamId, role, teamUserId);

        if (teamUserInfos.size() > PAGE_SIZE) {
            List<TeamUserDto> result = teamUserInfos.stream().limit(PAGE_SIZE).toList();
            return new TeamUserResponse(
                PAGE_SIZE,
                TRUE,
                teamUserInfos.get(PAGE_SIZE).role(),
                teamUserInfos.get(PAGE_SIZE).teamUserId(),
                result);
        }

        return new TeamUserResponse(
            teamUserInfos.size(),
            FALSE,
            null,
            null,
            teamUserInfos
        );
    }

    @TeamCoLeader
    @Transactional(readOnly = true)
    public List<TeamInviteDto> findTeamInviteRequest(Long userId, Long teamId) {
        return teamUserQuerydslRepository.getTeamInviteInfo(teamId);
    }

    private TeamInvite findTeamInviteById(Long teamInviteId) {
        return teamInviteJpaRepository.findById(teamInviteId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamUser findTeamUserById(Long teamUserId) {
        return teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private void leaderCantDownGradeValidation(TeamUser teamUser, Long userId) {
        if (teamUser.getUserId().equals(userId)) { // 방장이 자신의 등급을 낮추는 건 불가능
            throw new TeamException(INVALID_REQUEST);
        }
    }

    private void teamParameterValidation(TeamUserRole role, Long teamUserId) {
        if ((role != null && teamUserId == null) || (role == null && teamUserId != null)) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

}
