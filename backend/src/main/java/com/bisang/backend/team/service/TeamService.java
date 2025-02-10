package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.controller.response.TeamInfosResponse;
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

    @EveryOne
    @Transactional
    public Long createTeam(
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
        teamProfileUri = profileUriValidation(teamProfileUri);
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
        return newTeam.getId();
    }

    private String profileUriValidation(String teamProfileUri) {
        if (teamProfileUri == null) {
            return "eyJhbGciOiJIUzM4NCJ9."
                    + "eyJzdWIiOiIzIiwiaWF0IjoxNzM5MTQ4MjM0LCJleHAiOjE3MzkxNTE4MzQsInR5cGUiOiJhY2Nlc3NUb2tlbiJ9"
                    + ".fdVC6zw6EVJT84-jzTO-D3yvjW_oR_7-qoX8-qpNOe2aNsUdROeP0N3sYaKVa90J";
        }

        if (!teamProfileUri.startsWith("https://bisang-mimo-bucket.s3.ap-northeast-2.amazonaws.com/")) {
            throw new IllegalArgumentException("이미지가 서버 내에 존재하지 않습니다. 이미지 업로드 후 다시 요청해주세요.");
        }
        return teamProfileUri;
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamInfosResponse getTeamInfosByArea(Area area, Long teamId) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.getTeamsByAreaCode(area, teamId);
        Boolean hasNext = teams.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : teams.size();
        Long lastTeamId = size == 0 ? null : teams.get(size - 1).teamId();
        if (hasNext) {
            teams.remove(size - 1);
        }
        return new TeamInfosResponse(size, hasNext, lastTeamId, teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamInfosResponse getTeamInfosByCategory(TeamCategory category, Long teamId) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.getTeamsByCategory(category, teamId);
        Boolean hasNext = teams.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : teams.size();
        Long lastTeamId = teams.get(size - 1).teamId();
        if (hasNext) {
            teams.remove(size - 1);
        }
        return new TeamInfosResponse(size, hasNext, lastTeamId, teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamDto getTeamGeneralInfo(Long teamId) {
        return teamQuerydslRepository.getTeamInfo(teamId);
    }

    @TeamLeader
    @Transactional
    public void updateTeam(
            Long userId,
            Long teamId,
            String name,
            String description,
            TeamRecruitStatus recruitStatus,
            TeamPrivateStatus privateStatus,
            String profileUri,
            Area areaCode
    ) {
        Team team = findTeamById(teamId);
        team.updateTeamName(name);

        var teamDescription = team.getDescription();
        teamDescription.updateDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);
        team.updateShortDescription(description);

        team.updateRecruitStatus(recruitStatus);
        team.updatePrivateStatus(privateStatus);
        team.updateTeamProfileUri(profileUri);
        team.updateAreaCode(areaCode);

        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void deleteTeam(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        List<TeamUser> teamUsers = teamUserJpaRepository.findByTeamId(teamId);
        if (teamUsers.size() == 1) {
            teamUserJpaRepository.delete(teamUsers.get(0));
            teamJpaRepository.delete(team);
            return;
        }
        throw new TeamException(EXTRA_USER);
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
