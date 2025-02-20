package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.team.domain.TeamNotificationStatus.ACTIVE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.team.controller.dto.MyTeamSpecificDto;
import com.bisang.backend.team.domain.TeamTag;
import com.bisang.backend.team.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.common.utils.StringUtils;
import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.invite.repository.TeamInviteJpaRepository;
import com.bisang.backend.team.annotation.TeamCoLeader;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.controller.response.TeamUserResponse;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.domain.TeamUserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamLeaderService {
    private final TeamQuerydslRepository teamQuerydslRepository;
    private final TeamSearchQuerydslRepository teamSearchQuerydslRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamUserQuerydslRepository teamUserQuerydslRepository;
    private final TeamInviteJpaRepository teamInviteJpaRepository;
    private final ChatroomUserService chatroomUserService;
    private final TeamTagJpaRepository teamTagJpaRepository;

    @TeamLeader
    @Transactional
    public void deleteTag(Long userId, Long teamId, String tagName) {
        Optional<TeamTag> optionalTeamTag = teamTagJpaRepository.findByTeamIdAndTagName(teamId, tagName);
        if (optionalTeamTag.isPresent()) {
            teamTagJpaRepository.delete(optionalTeamTag.get());
            return;
        }
        throw new TeamException(NOT_FOUND);
    }

    @TeamLeader
    @Transactional
    public void addTag(Long userId, Long teamId, List<String> tags) {
        tags = tags.stream()
                    .distinct()
                    .toList();

        for (String tag : tags) {
            Optional<TeamTag> optionalTeamTag = teamTagJpaRepository.findByTeamIdAndTagName(teamId, tag);
            if (optionalTeamTag.isEmpty()) {
                teamTagJpaRepository.save(new TeamTag(teamId, tag));
            }
        }
    }

    @TeamLeader
    @Transactional(readOnly = true)
    public MyTeamSpecificDto getMyTeamSpecificDto(Long userId, Long teamId) {
        return teamQuerydslRepository.getMyTeamSpecificInfo(teamId);
    }

    @TeamCoLeader
    @Transactional
    public void approveInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInvite.approveInvitation();
        teamInviteJpaRepository.save(teamInvite);

        String tmpNickname = StringUtils.randomAlphaNumeric(10);
        TeamUser teamUser = TeamUser.createTeamMember(teamInvite.getUserId(), teamId, tmpNickname, ACTIVE);
        teamUserJpaRepository.save(teamUser);

        // CHATTING 방에 참여.
        chatroomUserService.enterChatroom(teamId, teamInvite.getUserId(), tmpNickname);
    }

    @TeamCoLeader
    @Transactional
    public void rejectInviteRequest(Long userId, Long teamId, Long inviteId) {
        TeamInvite teamInvite = findTeamInviteById(inviteId);
        teamInviteJpaRepository.delete(teamInvite);
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
        Team team = findTeamById(teamId);
        TeamUser teamUser = findTeamUserById(teamUserId);
        teamUserInThisTeam(teamId, teamUser);
        leaderCannotDeleteValidation(team, teamUser);
        teamUserJpaRepository.delete(teamUser);

        // 채팅방 유저 강퇴
        chatroomUserService.forceLeave(teamId, teamUser.getUserId());
        Optional<TeamInvite> teamInvite = teamInviteJpaRepository.findByTeamIdAndUserId(teamId, userId);
        if (teamInvite.isPresent()) {
            teamInviteJpaRepository.delete(teamInvite.get());
        }
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

    private void teamUserInThisTeam(Long teamId, TeamUser teamUser) {
        if (!teamUser.getTeamId().equals(teamId)) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

    private void leaderCannotDeleteValidation(Team team, TeamUser teamUser) {
        if (team.getTeamLeaderId().equals(teamUser.getUserId())) {
            throw new TeamException(NOT_DELETE_LEADER);
        }
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
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
