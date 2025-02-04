package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamDescription;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import com.bisang.backend.team.repository.TeamDescriptionJpaRepository;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamQuerydslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private static final Long MAX_TEAM_CAPACITY = 200L;

    private final TeamJpaRepository teamJpaRepository;
    private final TeamDescriptionJpaRepository teamDescriptionJpaRepository;
    private final TeamQuerydslRepository teamQuerydslRepository;

    @TeamLeader
    @Transactional
    public void createTeam(
            Long leaderId,
            String name,
            String description,
            TeamRecruitStatus teamRecruitStatus,
            TeamPrivateStatus teamPrivateStatus,
            String teamProfileUri,
            Area area
    ) {
        TeamDescription teamDescription = new TeamDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);

        // TODO: 채팅 룸 관련 생성 기능 추가
        // TODO: 계좌 관련 생성 기능 추가

        Team newTeam = Team.builder()
                            .teamLeaderId(leaderId)
                            .teamChatroomId(0L) // 추후 추가 필요, 챗룸 구현 이후
                            .name(name)
                            .description(teamDescription)
                            .accountNumber("1001111111111")
                            .recruitStatus(teamRecruitStatus)
                            .privateStatus(teamPrivateStatus)
                            .teamProfileUri(teamProfileUri)
                            .areaCode(area)
                            .capacity(MAX_TEAM_CAPACITY).build();
        teamJpaRepository.save(newTeam);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamDto getTeamGeneralInfo(Long teamId) {
        return teamQuerydslRepository.getTeamInfo(teamId);
    }

    @TeamLeader
    @Transactional
    public void updateTeamName(Long userId, Long teamId, String name) {
        Team team = findTeamById(teamId);

        if (isTeamLeader(team, userId)) {
            team.updateTeamName(name);
            teamJpaRepository.save(team);
        }
    }

    @TeamLeader
    @Transactional
    public void updateTeamDescription(Long userId, Long teamId, String description) {
        Team team = findTeamById(teamId);

        if (isTeamLeader(team, userId)) {
            var teamDescription = team.getDescription();
            teamDescription.updateDescription(description);
            teamDescriptionJpaRepository.save(teamDescription);
        }
    }

    @TeamLeader
    @Transactional
    public void updateTeamRecruitStatus(Long userId, Long teamId, TeamRecruitStatus recruitStatus) {
        Team team = findTeamById(teamId);
        if (isTeamLeader(team, userId)) {
            team.updateRecruitStatus(recruitStatus);
        }
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamPrivateStatus(Long userId, Long teamId, TeamPrivateStatus privateStatus) {
        Team team = findTeamById(teamId);
        if (isTeamLeader(team, userId)) {
            team.updatePrivateStatus(privateStatus);
        }
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamProfileUri(Long userId, Long teamId, String profileUri) {
        Team team = findTeamById(teamId);
        if (isTeamLeader(team, userId)) {
            team.updateTeamProfileUri(profileUri);
        }
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamArea(Long userId, Long teamId, Area areaCode) {
        Team team = findTeamById(teamId);
        if (isTeamLeader(team, userId)) {
            team.updateAreaCode(areaCode);
        }
        teamJpaRepository.save(team);
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND_TEAM));
    }

    private Boolean isTeamLeader(Team team, Long userId) {
        if (team.getTeamLeaderId().equals(userId)) {
            return true;
        }
        throw new TeamException(INVALID_REQUEST);
    }
}
