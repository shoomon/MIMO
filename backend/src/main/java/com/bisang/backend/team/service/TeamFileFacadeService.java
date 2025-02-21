package com.bisang.backend.team.service;

import com.bisang.backend.s3.domain.ProfileImage;
import com.bisang.backend.s3.repository.ProfileImageRepository;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.bisang.backend.s3.domain.ImageType.TEAM;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;

@Service
@RequiredArgsConstructor
public class TeamFileFacadeService {
    private final ProfileImageRepository profileImageRepository;
    private final TeamService teamService;
    private final S3Service s3Service;

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
        return teamService.createTeam(
            leaderId, nickname, notificationStatus,
            name, description, teamRecruitStatus, teamPrivateStatus,
            teamProfileUri, area, teamCategory, maxCapacity);
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
        String areaCode,
        String category
    ) {
        String profileUri = getProfileUriForUpdate(teamId, profile);
        teamService.updateTeam(
            userId, teamId, name, description,
            recruitStatus, privateStatus, profileUri, Area.fromName(areaCode), TeamCategory.fromName(category));
    }

    private String profileUriValidation(Long userId, MultipartFile teamProfile) {
        if (teamProfile == null) {
            return CAT_IMAGE_URI;
        }
        return s3Service.saveFile(userId, teamProfile);
    }

    private String getProfileUriForUpdate(Long teamId, MultipartFile profile) {
        if (profile == null) {
            return null;
        }
        return s3Service.saveFile(teamId, profile);
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
            try {
                if (!profileImage.get().getProfileUri().equals(CAT_IMAGE_URI)) {
                    s3Service.deleteFile(profileImage.get().getProfileUri());
                }
            } catch (Exception e) { // S3 서버에 없는 구글 파일이라 생긴 모든 삭제 실패는 무시.
            }
        }
        return s3Service.saveFile(teamId, profile);
    }

}

