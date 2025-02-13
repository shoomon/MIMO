package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.invite.domain.TeamInvite.createInviteRequest;
import static com.bisang.backend.team.domain.TeamRecruitStatus.ACTIVE_PRIVATE;
import static com.bisang.backend.team.domain.TeamRecruitStatus.ACTIVE_PUBLIC;
import static com.bisang.backend.team.domain.TeamUser.createTeamMember;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.invite.repository.TeamInviteJpaRepository;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.controller.response.SingleTeamUserInfoResponse;
import com.bisang.backend.team.controller.response.TeamUserResponse;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.domain.TeamUserRole;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.team.repository.TeamUserQuerydslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamInviteJpaRepository teamInviteJpaRepository;
    private final TeamUserQuerydslRepository teamUserQuerydslRepository;

    @EveryOne
    public Boolean existsNicknameByTeamIdAndNickname(Long teamId, String nickname) {
        return teamUserJpaRepository.existsByTeamIdAndNickname(teamId, nickname);
    }

    @EveryOne
    @Transactional
    public void joinTeam(Long userId, Long teamId, String nickname, TeamNotificationStatus status) {
        isAlreadyJoinChecker(teamId, userId);

        Team team = findTeamById(teamId);
        Long currentUserCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        if (team.getMaxCapacity() > currentUserCount) {
            if (team.getRecruitStatus() == ACTIVE_PUBLIC) {
                TeamUser newTeamMember = createTeamMember(userId, teamId, nickname, status);
                teamUserJpaRepository.save(newTeamMember);
                return;
            } else if (team.getRecruitStatus() == ACTIVE_PRIVATE) {
                throw new TeamException(NOT_PUBLIC_TEAM);
            }
            throw new TeamException(NOT_RECRUIT_TEAM);
        }
        throw new TeamException(FULL_TEAM);
    }

    @EveryOne
    @Transactional
    public void inviteRequest(Long userId, Long teamId, String memo) {
        isAlreadyJoinChecker(teamId, userId);
        isAlreadyRequestedChecker(teamId, userId);

        Team team = findTeamById(teamId);
        Long currentUserCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        Long currentInviteCount = teamInviteJpaRepository.countByTeamId(teamId);
        if (team.getMaxCapacity() > currentUserCount + currentInviteCount) {
            if (team.getRecruitStatus() == ACTIVE_PRIVATE) {
                TeamInvite inviteRequest = createInviteRequest(teamId, userId, memo);
                teamInviteJpaRepository.save(inviteRequest);
                return;
            } else if (team.getRecruitStatus() == ACTIVE_PUBLIC) {
                throw new TeamException(NOT_PRIVATE_TEAM);
            }
            throw new TeamException(NOT_RECRUIT_TEAM);
        }
        throw new TeamException(FULL_TEAM_INVITE);
    }

    @Transactional
    public void updateNickname(Long userId, Long teamId, String nickname) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        teamUser.updateNickname(nickname);
        teamUserJpaRepository.save(teamUser);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamUserResponse findTeamUsers(Long userId, Long teamId, TeamUserRole role, Long teamUserId) {
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

    @Transactional(readOnly = true)
    public SingleTeamUserInfoResponse getSingleTeamUserInfo(Long userId, Long teamId) {
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);

        return SingleTeamUserInfoResponse.builder()
            .teamUserId(teamUser.getId())
            .nickname(teamUser.getNickname())
            .role(teamUser.getRole())
            .status(teamUser.getStatus())
            .joinDate(teamUser.getCreatedAt().toLocalDate())
            .build();
    }

    /**
     * 글은 남아있는데 탈퇴한 회원은 어떻게 하지? -> left join으로 처리
     */
    @Transactional
    public void deleteUser(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, userId);
        leaderValidation(team, teamUser);
        teamUserJpaRepository.delete(teamUser);
    }

    private static void leaderValidation(Team team, TeamUser teamUser) {
        if (team.getTeamLeaderId().equals(teamUser.getUserId())) {
            throw new TeamException(NOT_DELETE_LEADER);
        }
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
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamUser findTeamUserByTeamIdAndUserId(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
