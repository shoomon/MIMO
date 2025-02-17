package com.bisang.backend.team.service;

import static com.bisang.backend.chat.domain.ChatroomStatus.GROUP;
import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.s3.domain.ImageType.TEAM;
import static com.bisang.backend.s3.domain.ProfileImage.createTeamProfile;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;
import static com.bisang.backend.team.domain.Area.fromName;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.team.controller.dto.TagDto;
import com.bisang.backend.team.controller.response.TeamTagResponse;
import com.bisang.backend.team.controller.response.TeamTagSearchResponse;
import com.bisang.backend.team.controller.response.TeamTitleDescSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.s3.domain.ProfileImage;
import com.bisang.backend.s3.repository.ProfileImageRepository;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.account.service.AccountService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.chat.service.ChatroomService;
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
    private final AccountService accountService;
    private final TagJpaRepository tagJpaRepository;
    private final TeamDescriptionJpaRepository teamDescriptionJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamQuerydslRepository teamQuerydslRepository;
    private final TeamTagJpaRepository teamTagJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final ChatroomService chatroomService;
    private final ChatroomUserService chatroomUserService;

    @EveryOne
    public Boolean existsTeamByName(String name) {
        return teamJpaRepository.existsByName(name);
    }

    @EveryOne
    public Long createTeam(
            Long leaderId,
            String nickname,
            TeamNotificationStatus notificationStatus,
            String name,
            String description,
            TeamRecruitStatus teamRecruitStatus,
            TeamPrivateStatus teamPrivateStatus,
            MultipartFile teamProfile,
            String area,
            String teamCategory,
            Long maxCapacity
    ) {
        String teamProfileUri = profileUriValidation(leaderId, teamProfile);
        Long createdTeamId = null;
        try {
            createdTeamId = createTeam(
                    leaderId, nickname, notificationStatus,
                    name, description, teamRecruitStatus, teamPrivateStatus,
                    teamProfileUri, area, teamCategory, maxCapacity);
        } catch (TeamException e) {
            if (!teamProfileUri.equals(CAT_IMAGE_URI)) {
                s3Service.deleteFile(teamProfileUri);
            }
            throw new TeamException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            if (!teamProfileUri.equals(CAT_IMAGE_URI)) {
                s3Service.deleteFile(teamProfileUri);
            }
            throw new TeamException(DUPLICATED_SOURCE);
        }
        return createdTeamId;
    }

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
            String area,
            String teamCategory,
            Long maxCapacity
    ) {
        // 팀 설명 생성
        TeamDescription teamDescription = new TeamDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);

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
                            .areaCode(Area.fromName(area))
                            .category(TeamCategory.fromName(teamCategory))
                            .maxCapacity(maxCapacity).build();
        teamJpaRepository.save(newTeam);
        // 프로필 이미지 저장, 기본 고양이, profile 있을 시 S3에 업로드 된 해당 프로필 이미지 경로
        profileImageRepository.save(createTeamProfile(newTeam.getId(), teamProfileUri));

        // 기본 태그 저장
        Tag areaTag = findTagByName(area);
        TeamTag areaTeamTag = new TeamTag(newTeam.getId(), areaTag.getId());
        teamTagJpaRepository.save(areaTeamTag);

        Tag categoryTag = findTagByName(teamCategory);
        TeamTag categoryTeamTag = new TeamTag(newTeam.getId(), categoryTag.getId());
        teamTagJpaRepository.save(categoryTeamTag);

        // 팀 유저 저장
        var teamUser = TeamUser.createTeamLeader(leaderId, newTeam.getId(), nickname, notificationStatus);
        teamUserJpaRepository.save(teamUser);

        // 채팅 룸 생성
        chatroomService.createChatroom(
                leaderId,
                newTeam.getId(),
                nickname,
                name + "모임의 채팅방",
                teamProfileUri,
                GROUP
        );

        return newTeam.getId();
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamInfosResponse getTeamInfosByArea(Area area, Long teamId) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.getTeamsByAreaCode(area, teamId);
        Boolean hasNext = teams.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : teams.size();
        Long lastTeamId = hasNext ? teams.get(size - 1).teamId() : null;
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
        Long lastTeamId = hasNext ? teams.get(size - 1).teamId() : null;
        if (hasNext) {
            teams.remove(size - 1);
        }
        return new TeamInfosResponse(size, hasNext, lastTeamId, teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamDto getTeamGeneralInfo(Long userId, Long teamId) {
        return teamQuerydslRepository.getTeamInfo(userId, teamId);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public SimpleTeamDto getSimpleTeamInfo(Long userId, Long teamId) {
        return teamQuerydslRepository.getSimpleTeamInfo(userId, teamId);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTitleDescSearchResponse getTeamsByTitleOrDescription(String searchKeyword, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.searchTeams(searchKeyword, pageNumber);
        Long numberOfTeams = teamQuerydslRepository.searchTeamsCount(searchKeyword);
        return new TeamTitleDescSearchResponse(numberOfTeams.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagSearchResponse getTeamsByTag(Long tagId, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.searchTeams(tagId, pageNumber);
        Long teamsCount = teamQuerydslRepository.searchTeamsCount(tagId);
        return new TeamTagSearchResponse(teamsCount.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagResponse getTagBySearchKeyword(String searchKeyword, Integer pageNumber) {
        List<TagDto> tags = teamQuerydslRepository.searchTags(searchKeyword, pageNumber);
        Long numberOfTags = teamQuerydslRepository.searchTagsCount(searchKeyword);
        return new TeamTagResponse(numberOfTags.intValue(), pageNumber, tags.size(), tags);
    }

    @TeamLeader
    public void updateTeam(
            Long userId,
            Long teamId,
            String name,
            String description,
            TeamRecruitStatus recruitStatus,
            TeamPrivateStatus privateStatus,
            MultipartFile profile,
            Area areaCode,
            TeamCategory category
    ) {
        String profileUri = getProfileUri(teamId, profile);
        try {
            updateTeam(
                    userId, teamId, name, description,
                    recruitStatus, privateStatus, profileUri, areaCode, category);
        } catch (TeamException e) {
            if (!profileUri.equals(CAT_IMAGE_URI)) {
                s3Service.deleteFile(profileUri);
            }
            throw new TeamException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            if (!profileUri.equals(CAT_IMAGE_URI)) {
                s3Service.deleteFile(profileUri);
            }
            throw new TeamException(INVALID_REQUEST);
        }
    }

    @Transactional
    public void updateTeam(
            Long userId,
            Long teamId,
            String name,
            String description,
            TeamRecruitStatus recruitStatus,
            TeamPrivateStatus privateStatus,
            String profileUri,
            Area areaCode,
            TeamCategory category
    ) {
        profileImageRepository.save(createTeamProfile(teamId, profileUri));

        Team team = findTeamById(teamId);
        team.updateTeamName(name);
        // 채팅방 이름 변경
        chatroomService.updateChatroomTitle(teamId, name + "모임의 채팅방");

        var teamDescription = team.getDescription();
        teamDescription.updateDescription(description);
        teamDescriptionJpaRepository.save(teamDescription);
        team.updateDescription(description);

        team.updateRecruitStatus(recruitStatus);
        team.updatePrivateStatus(privateStatus);

        if (category != team.getCategory()) {
            deleteOldCategoryTeamTag(team);
            saveNewCategoryTeamTag(teamId, category);
            team.updateCategory(category);
        }

        if (areaCode != team.getAreaCode()) {
            deleteOldAreaTeamTag(team);
            saveNewAreaTeamTag(teamId, areaCode);
            team.updateAreaCode(areaCode);
        }

        team.updateTeamProfileUri(profileUri);
        // 채팅방 프로필 변경
        chatroomService.updateChatroomProfileUri(teamId, profileUri);

        teamJpaRepository.save(team);
    }

    @TeamLeader
    @Transactional
    public void deleteTeam(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        List<TeamUser> teamUsers = teamUserJpaRepository.findByTeamId(teamId);
        if (teamUsers.size() == 1) {
            profileImageRepository.deleteTeamImageByImageTypeAndTeamId(TEAM, teamId);
            teamUserJpaRepository.delete(teamUsers.get(0));
            teamJpaRepository.delete(team);
            return;
        }
        throw new TeamException(EXTRA_USER);
    }

    private String getProfileUri(Long teamId, MultipartFile profile) {
        Optional<ProfileImage> profileImage = profileImageRepository.findTeamImageByImageTypeAndTeamId(TEAM, teamId);
        if (profile == null) {
            if (profileImage.isEmpty()) {
                return CAT_IMAGE_URI;
            }
            return profileImage.get().getProfileUri();
        }
        if (profileImage.isPresent()) {
            profileImageRepository.delete(profileImage.get());
            try {
                if (!profileImage.get().getProfileUri().equals(CAT_IMAGE_URI)) {
                    s3Service.deleteFile(profileImage.get().getProfileUri());
                }
            } catch (Exception e) { // S3 서버에 없는 구글 파일이라 생긴 모든 삭제 실패는 무시.
            }
        }
        return s3Service.saveFile(teamId, profile);
    }

    private String profileUriValidation(Long userId, MultipartFile teamProfile) {
        if (teamProfile == null) {
            return CAT_IMAGE_URI;
        }
        return s3Service.saveFile(userId, teamProfile);
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private Tag findTagByName(String name) {
        return tagJpaRepository.findByName(name)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private void saveNewCategoryTeamTag(Long teamId, TeamCategory category) {
        Tag categoryTag = findTagByName(category.getName());
        TeamTag categoryTeamTag = new TeamTag(teamId, categoryTag.getId());
        teamTagJpaRepository.save(categoryTeamTag);
    }

    private void saveNewAreaTeamTag(Long teamId, Area areaCode) {
        Tag areaTag = findTagByName(areaCode.getName());
        TeamTag areaTeamTag = new TeamTag(teamId, areaTag.getId());
        teamTagJpaRepository.save(areaTeamTag);
    }

    private void deleteOldCategoryTeamTag(Team team) {
        TeamCategory oldCategory = team.getCategory();
        Tag oldCategoryTag = getOldTagByName(oldCategory.getName());
        TeamTag savedCategoryTeamTag = getSavedTeamTagByTag(team.getId(), oldCategoryTag);
        teamTagJpaRepository.delete(savedCategoryTeamTag);
    }

    private void deleteOldAreaTeamTag(Team team) {
        Area oldArea = team.getAreaCode();
        Tag oldAreaTag = getOldTagByName(oldArea.getName());
        TeamTag savedAreaTeamTag = getSavedTeamTagByTag(team.getId(), oldAreaTag);
        teamTagJpaRepository.delete(savedAreaTeamTag);
    }

    private Tag getOldTagByName(String name) {
        return tagJpaRepository.findByName(name)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamTag getSavedTeamTagByTag(Long teamId, Tag oldTag) {
        return teamTagJpaRepository.findByTeamIdAndTagId(teamId, oldTag.getId())
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
