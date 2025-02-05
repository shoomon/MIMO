package com.bisang.backend.user.domain;

import static com.bisang.backend.user.domain.UserStatus.ACTIVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Table(name = "USERS")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", length = 40, unique = true)
    private String email;

    @Column(name = "user_name", nullable = false, length = 10)
    private String name;

    @Column(name = "user_nickname", nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = "user_profile_uri", nullable = false)
    private String profileUri;

    @Enumerated(STRING)
    @Column(name = "user_status")
    private UserStatus status;

    @Builder
    protected User(
            String email,
            String name,
            String nickname,
            String profileUri
    ) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileUri = profileUri;
        this.status = ACTIVE;
    }

    public User createNewSocialUser(
            String email,
            String name,
            String nickname,
            String profileUri
    ) {
        return new User(email, name, nickname, profileUri);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }
}
