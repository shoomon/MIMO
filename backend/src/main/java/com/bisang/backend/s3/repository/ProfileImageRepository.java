package com.bisang.backend.s3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.s3.domain.ImageType;
import com.bisang.backend.s3.domain.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findUserImageByImageTypeAndUserId(ImageType imageType, Long userId);

    Optional<ProfileImage> findTeamImageByImageTypeAndTeamId(ImageType imageType, Long teamId);

    void deleteTeamImageByImageTypeAndTeamId(ImageType imageType, Long teamId);
}
