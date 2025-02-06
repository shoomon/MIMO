package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.Tag;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.domain.TeamDescription;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import com.bisang.backend.team.domain.TeamTag;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TagJpaRepository;
import com.bisang.backend.team.repository.TeamDescriptionJpaRepository;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamQuerydslRepository;
import com.bisang.backend.team.repository.TeamTagJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TagJpaRepository tagJpaRepository;
    private final TeamDescriptionJpaRepository teamDescriptionJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamQuerydslRepository teamQuerydslRepository;
    private final TeamTagJpaRepository teamTagJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    @Transactional
    public void createTeam(
            Long leaderId,
            String nickname,
            TeamNotificationStatus notificationStatus,
            String name,
            String description,
            TeamRecruitStatus teamRecruitStatus,
            TeamPrivateStatus teamPrivateStatus,
            String teamProfileUri,
            Area area,
            TeamCategory teamCategory,
            Long maxCapacity
    ) {
        // 팀 설명 생성
        TeamDescription teamDescription = new TeamDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);

        // TODO: 채팅 룸 관련 생성 기능 추가
        // TODO: 계좌 관련 생성 기능 추가

        // 팀 생성
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
                            .category(teamCategory)
                            .maxCapacity(maxCapacity).build();
        teamJpaRepository.save(newTeam);

        // 기본 태그 저장
        Tag areaTag = findTagByName(area.getName());
        TeamTag areaTeamTag = new TeamTag(newTeam.getId(), areaTag.getId());
        teamTagJpaRepository.save(areaTeamTag);

        Tag categoryTag = findTagByName(teamCategory.getName());
        TeamTag categoryTeamTag = new TeamTag(newTeam.getId(), categoryTag.getId());
        teamTagJpaRepository.save(categoryTeamTag);

        // 팀 유저 저장
        var teamUser = TeamUser.createTeamLeader(leaderId, newTeam.getId(), nickname, notificationStatus);
        teamUserJpaRepository.save(teamUser);
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
        team.updateTeamName(name);
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamDescription(Long userId, Long teamId, String description) {
        Team team = findTeamById(teamId);

        var teamDescription = team.getDescription();
        teamDescription.updateDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);
    }

    @TeamLeader
    @Transactional
    public void updateTeamRecruitStatus(Long userId, Long teamId, TeamRecruitStatus recruitStatus) {
        Team team = findTeamById(teamId);
        team.updateRecruitStatus(recruitStatus);
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamPrivateStatus(Long userId, Long teamId, TeamPrivateStatus privateStatus) {
        Team team = findTeamById(teamId);
        team.updatePrivateStatus(privateStatus);
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamProfileUri(Long userId, Long teamId, String profileUri) {
        Team team = findTeamById(teamId);
        team.updateTeamProfileUri(profileUri);
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void updateTeamArea(Long userId, Long teamId, Area areaCode) {
        Team team = findTeamById(teamId);
        team.updateAreaCode(areaCode);
        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void deleteTeam(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        List<TeamUser> teamUsers = teamUserJpaRepository.findByTeamId(teamId);
        if (teamUsers.size() == 1) {
            // TODO 계좌 삭제
            // TODO 채팅방 삭제
            teamUserJpaRepository.delete(teamUsers.get(0));
            teamJpaRepository.delete(team);
        }
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private Tag findTagByName(String name) {
        return tagJpaRepository.findByName(name)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
