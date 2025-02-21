package com.bisang.backend.team.service;

import static com.bisang.backend.chat.domain.ChatroomStatus.GROUP;
import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.s3.domain.ImageType.TEAM;
import static com.bisang.backend.s3.domain.ProfileImage.createTeamProfile;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;
import com.bisang.backend.team.domain.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
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
    private final TeamDescriptionJpaRepository teamDescriptionJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamQuerydslRepository teamQuerydslRepository;
    private final TeamTagJpaRepository teamTagJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final ChatroomService chatroomService;
    private final ChatroomUserService chatroomUserService;

    @EveryOne
    public Boolean existsTeamByName(String name) {
        return teamJpaRepository.existsByName(name);
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
        if (Area.fromName(area) == null || TeamCategory.fromName(teamCategory) == null) {
            throw new TeamException(INVALID_REQUEST);
        }

        Team newTeam = null;
        try {
            // 팀 설명 생성
            TeamDescription teamDescription = new TeamDescription(description);
            teamDescriptionJpaRepository.save(teamDescription);

            // 계좌 생성
            String accountNumber = accountService.createTeamAccount();

            // 팀 생성
            newTeam = Team.builder()
                    .teamLeaderId(leaderId)
                    .teamChatroomId(0L) // 추후 추가 필요, 챗룸 구현 이후
                    .name(name)
                    .description(teamDescription)
                    .accountNumber(accountNumber)
                    .recruitStatus(teamRecruitStatus)
                    .privateStatus(teamPrivateStatus)
                    .teamProfileUri(teamProfileUri)
                    .teamRound(0L)
                    .areaCode(Area.fromName(area))
                    .category(TeamCategory.fromName(teamCategory))
                    .maxCapacity(maxCapacity).build();
            teamJpaRepository.save(newTeam);
            // 프로필 이미지 저장, 기본 고양이, profile 있을 시 S3에 업로드 된 해당 프로필 이미지 경로
            profileImageRepository.save(createTeamProfile(newTeam.getId(), teamProfileUri));

            TeamTag areaTag = new TeamTag(newTeam.getId(), area);
            teamTagJpaRepository.save(areaTag);
            TeamTag categoryTag = new TeamTag(newTeam.getId(), teamCategory);
            teamTagJpaRepository.save(categoryTag);

            //기본 게시판 생성
            teamBoardJpaRepository.save(TeamBoard.builder()
                    .teamId(newTeam.getId())
                    .boardName("자유게시판")
                    .build()
            );

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
            teams = teams.stream()
                    .limit(size)
                    .toList();
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
            teams = teams.stream()
                    .limit(size)
                    .toList();
        }
        return new TeamInfosResponse(size, hasNext, lastTeamId, teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamDto getTeamGeneralInfo(Long userId, Long teamId) {
        privateTeamValidation(userId, teamId);
        return teamQuerydslRepository.getTeamInfo(userId, teamId);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public SimpleTeamDto getSimpleTeamInfo(Long userId, Long teamId) {
        privateTeamValidation(userId, teamId);
        return teamQuerydslRepository.getSimpleTeamInfo(userId, teamId);
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
        try {
            if (profileUri != null) {
                var profile = profileImageRepository.findTeamImageByImageTypeAndTeamId(TEAM, teamId);
                profile.ifPresent(profileImageRepository::delete);
                profileImageRepository.save(createTeamProfile(teamId, profileUri));
            }

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
                if (teamTagJpaRepository.findByTeamIdAndTagName(team.getId(), category.getName()).isEmpty()) {
                    teamTagJpaRepository.save(new TeamTag(team.getId(), category.getName()));
                }
                teamTagJpaRepository.findByTeamIdAndTagName(team.getId(), team.getCategory().getName())
                        .ifPresent(teamTagJpaRepository::delete);
                team.updateCategory(category);
            }

            if (areaCode != team.getAreaCode()) {
                Optional<TeamTag> teamTag = teamTagJpaRepository.findByTeamIdAndTagName(team.getId(), areaCode.getName());
                if (teamTag.isEmpty()) {
                    teamTagJpaRepository.save(new TeamTag(team.getId(), areaCode.getName()));
                }
                teamTagJpaRepository.findByTeamIdAndTagName(team.getId(), team.getAreaCode().getName())
                        .ifPresent(teamTagJpaRepository::delete);
                team.updateAreaCode(areaCode);
            }

            if (profileUri != null) {
                team.updateTeamProfileUri(profileUri);
                // 채팅방 프로필 변경
                chatroomService.updateChatroomProfileUri(teamId, profileUri);
            }
            teamJpaRepository.save(team);
        } catch (TeamException e) {
            if (profileUri == null) {
                throw new TeamException(e.getCode(), e.getMessage());
            }
            if (!profileUri.equals(CAT_IMAGE_URI)) {
                s3Service.deleteFile(profileUri);
            }
            throw new TeamException(e.getCode(), e.getMessage());
        } catch (DataIntegrityViolationException e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
            throw new TeamException(1003, "모임명이 중복됩니다. 모임명을 변경해주세요.");
        } catch (NullPointerException e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
            throw new TeamException(1000, "지역과 카테고리에 있어 유효하지 않은 주소입니다.");
        }

//        catch (Exception e) {
//            if (profileUri == null) {
//                throw new TeamException(DUPLICATED_SOURCE);
//            }
//
//            if (!profileUri.equals(CAT_IMAGE_URI)) {
//                s3Service.deleteFile(profileUri);
//            }
//            throw new TeamException(INVALID_REQUEST);
//        }
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
            chatroomUserService.leaveChatroom(userId, teamId);
            return;
        }
        throw new TeamException(EXTRA_USER);
    }

    private void privateTeamValidation(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        if (team.getPrivateStatus().equals(PRIVATE)) {
            guestUserValidation(userId);
            findTeamUser(teamId, userId);
        }
    }

    private void guestUserValidation(Long userId) {
        if (userId == null) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

    private TeamUser findTeamUser(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(INVALID_REQUEST));
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
