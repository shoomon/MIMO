package com.bisang.backend.user.domain;

import static com.bisang.backend.user.domain.UserStatus.ACTIVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import org.apache.commons.lang3.Validate;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Table(name = "USERS",
        indexes = {
                @Index(name = "idx_user_id_accountNumber", columnList = "user_id, account_number")
        }
)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "account_number", length = 13, unique = true)
    private String accountNumber;

    @Column(name = "user_email", length = 40, unique = true)
    private String email;

    @Column(name = "user_name", nullable = false, length = 30)
    private String name;

    @Column(name = "user_nickname", nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(name = "user_profile_uri", nullable = false)
    private String profileUri;

    @Enumerated(STRING)
    @Column(name = "user_status")
    private UserStatus status;

    @Builder
    public User(
            String accountNumber,
            String email,
            String name,
            String nickname,
            String profileUri
    ) {
        this.accountNumber = accountNumber;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileUri = profileUri;
        this.status = ACTIVE;
    }

    public void updateName(String name) {
        String pattern = "^[a-zA-Z0-9가-힣]{1,30}$";
        Validate.matchesPattern(name, pattern,
                "이름은 영문, 숫자, 한글로만 구성되어 있으며, 길이는 1자리 이상 30자리 이하이어야 합니다.");
        this.name = name;
    }

    public void updateNickname(String nickname) {
        String pattern = "^[a-zA-Z0-9가-힣]{1,30}$";
        Validate.matchesPattern(nickname, pattern,
                "닉네임은 영문, 숫자, 한글로만 구성되어 있으며, 길이는 1자리 이상 30자리 이하이어야 합니다.");
        this.nickname = nickname;
    }

    public void updateProfileUri(String profileUri) {
        if (!profileUri.startsWith("https://bisang-mimo-bucket.s3.ap-northeast-2.amazonaws.com/")) {
            throw new IllegalArgumentException("이미지가 서버 내에 존재하지 않습니다. 이미지 업로드 후 다시 요청해주세요.");
        }
        this.profileUri = profileUri;
    }
}
