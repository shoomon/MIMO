package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.invite.domain.TeamInvite.createInviteRequest;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;
import static com.bisang.backend.team.domain.TeamRecruitStatus.ACTIVE_PRIVATE;
import static com.bisang.backend.team.domain.TeamRecruitStatus.ACTIVE_PUBLIC;
import static com.bisang.backend.team.domain.TeamUser.createTeamMember;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.team.repository.TeamReviewJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.invite.repository.TeamInviteJpaRepository;
import com.bisang.backend.invite.repository.TeamInviteQuerydslRepository;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.MyTeamUserInfoDto;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.controller.response.SingleTeamUserInfoResponse;
import com.bisang.backend.team.controller.response.TeamInfosResponse;
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
    private final TeamInviteQuerydslRepository teamInviteQuerydslRepository;
    private final TeamUserQuerydslRepository teamUserQuerydslRepository;
    private final ChatroomUserService chatroomUserService;
    private final TeamReviewJpaRepository teamReviewJpaRepository;

    @Transactional(readOnly = true)
    public TeamInfosResponse getMyTeamInfos(Long userId, Long teamId) {
        List<SimpleTeamDto> teamInfos = teamUserQuerydslRepository.getTeamsByTeamIdAndUserId(teamId, userId);
        Boolean hasNext = teamInfos.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : teamInfos.size();
        Long lastTeamId = hasNext ? teamInfos.get(size - 1).teamId() : null;
        if (hasNext) {
            teamInfos = teamInfos.stream()
                .limit(size)
                .toList();
        }
        return new TeamInfosResponse(size, hasNext, lastTeamId, teamInfos);
    }

    @Transactional(readOnly = true)
    public MyTeamUserInfoDto getMyTeamUserInfo(Long teamId, Long userId) {
        Optional<TeamUser> teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId);
        Optional<TeamInvite> teamInvite = teamInviteJpaRepository.findByTeamIdAndUserIdAndStatus(teamId, userId);
        Team team = findTeamById(teamId);
        Boolean hasReview = false;
        if (teamUser.isPresent()) {
            hasReview = teamReviewJpaRepository.existsTeamReviewByTeamUserId(teamUser.get().getId());
        }
        return MyTeamUserInfoDto.teamUserToDto(teamUser, team, teamInvite, hasReview);
    }

    @EveryOne
    public Boolean existsNicknameByTeamIdAndNickname(Long userId, Long teamId, String nickname) {
        privateGuestValidation(userId, teamId);
        return teamUserJpaRepository.existsByTeamIdAndNickname(teamId, nickname);
    }

    @EveryOne
    @Transactional
    public Long joinTeam(Long userId, Long teamId, String nickname, TeamNotificationStatus status) {
        isAlreadyJoinChecker(teamId, userId);

        Team team = findTeamById(teamId);
        Long currentUserCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        if (team.getMaxCapacity() > currentUserCount) {
            if (team.getRecruitStatus() == ACTIVE_PUBLIC) {
                TeamUser newTeamMember = createTeamMember(userId, teamId, nickname, status);
                try {
                    teamUserJpaRepository.save(newTeamMember);
                } catch (DataIntegrityViolationException e) {
                    throw new TeamException(1003, "모임 내에 중복된 닉네임이 존재합니다.");
                }

                // 채팅 방 가입 추가
                chatroomUserService.enterChatroom(teamId, userId, nickname);
                return newTeamMember.getId();
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
        Long currentInviteCount = teamInviteQuerydslRepository.countTeamInvite(teamId);
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
        try {
            teamUserJpaRepository.save(teamUser);
        } catch (DataIntegrityViolationException e) {
            throw new TeamException(1003, "모임 내에 중복된 닉네임이 존재합니다.");
        }

        // 채팅방 내의 닉네임 변경
        chatroomUserService.updateNickname(userId, teamId, nickname);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamUserResponse findTeamUsers(Long userId, Long teamId, TeamUserRole role, Long teamUserId) {
        privateGuestValidation(userId, teamId);

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
        privateGuestValidation(userId, teamId);

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

        // 채팅방 탈퇴 부분 추가
        chatroomUserService.leaveChatroom(userId, teamId);
    }

    private void privateGuestValidation(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        if (team.getPrivateStatus().equals(PRIVATE)) {
            if (userId == null) {
                throw new TeamException(INVALID_REQUEST);
            }
            findTeamUserByTeamIdAndUserId(teamId, userId);
        }
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
