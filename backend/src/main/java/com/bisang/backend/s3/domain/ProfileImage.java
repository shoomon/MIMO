package com.bisang.backend.s3.domain;

import static com.bisang.backend.s3.domain.ImageType.TEAM;
import static com.bisang.backend.s3.domain.ImageType.USER;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "profile_image",
        indexes = {
            @Index(name = "idx_profile_img_type_team_uri", columnList = "image_type, team_id, profile_uri"),
            @Index(name = "idx_profile_img_type_user_uri", columnList = "image_type, user_id, profile_uri")
        }
)
public class ProfileImage {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    @Enumerated(STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "profile_uri", nullable = false)
    private String profileUri;

    private ProfileImage(ImageType imageType, Long teamId, Long userId, String profileUri) {
        this.imageType = imageType;
        this.teamId = teamId;
        this.userId = userId;
        this.profileUri = profileUri;
    }

    public static ProfileImage createUserProfile(Long userId, String profileUri) {
        return new ProfileImage(USER, null, userId, profileUri);
    }

    public static ProfileImage createTeamProfile(Long teamId, String profileUri) {
        return new ProfileImage(TEAM, teamId, null, profileUri);
    }
}
