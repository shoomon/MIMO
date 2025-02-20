package com.bisang.backend.team.domain;

import static com.bisang.backend.team.domain.TeamUserRole.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_user",
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_team_user", columnNames = {"team_id", "user_id"}),
            @UniqueConstraint(name = "UK_team_nickname", columnNames = {"team_id", "nickname"})
        },
        indexes = {
                @Index(name = "idx_user_team", columnList = "user_id, team_id desc"),
                @Index(name = "idx_team_user", columnList = "team_id, user_id")
        }
)
public class TeamUser {

    @Id @Column(name = "team_user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long teamId;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(name = "team_user_role", nullable = false)
    @Enumerated(STRING)
    private TeamUserRole role;

    @Column(name = "notification_status", nullable = false)
    @Enumerated(STRING)
    private TeamNotificationStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private TeamUser(
            Long userId,
            Long teamId,
            String nickname,
            TeamUserRole role,
            TeamNotificationStatus status
    ) {
        String pattern = "^[a-zA-Z0-9가-힣]{1,30}$";
        Validate.matchesPattern(nickname, pattern,
            "모임 유저의 닉네임은 30자 이하의 영문, 숫자, 한글로 이루어져 있으며 ㅇㅇㅇ 같은 문자와 띄어쓰기는 허용하지 않습니다.");
        this.userId = userId;
        this.teamId = teamId;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
    }

    public static TeamUser createTeamLeader(
            Long userId,
            Long teamId,
            String nickname,
            TeamNotificationStatus status
    ) {
        return TeamUser.builder()
                .userId(userId)
                .teamId(teamId)
                .nickname(nickname)
                .role(LEADER)
                .status(status).build();
    }

    public static TeamUser createTeamCoLeader(
            Long userId,
            Long teamId,
            String nickname,
            TeamNotificationStatus status
    ) {
        return TeamUser.builder()
                .userId(userId)
                .teamId(teamId)
                .nickname(nickname)
                .role(CO_LEADER)
                .status(status).build();
    }

    public static TeamUser createTeamMember(
            Long userId,
            Long teamId,
            String nickname,
            TeamNotificationStatus status
    ) {
        return TeamUser.builder()
                .userId(userId)
                .teamId(teamId)
                .nickname(nickname)
                .role(MEMBER)
                .status(status).build();
    }

    public void updateNickname(String nickname) {
        String pattern = "^[a-zA-Z0-9가-힣]{1,30}$";
        Validate.matchesPattern(nickname, pattern,
                "모임 유저의 닉네임은 30자 이하의 영문, 숫자, 한글로 이루어져 있으며 ㅇㅇㅇ 같은 문자와 띄어쓰기는 허용하지 않습니다.");
        this.nickname = nickname;
    }

    public void upgradeRole() {
        this.role = CO_LEADER;
    }

    public void downgradeRole() {
        this.role = MEMBER;
    }

    public Boolean isCoLeader() {
        return role == CO_LEADER;
    }

    public Boolean isLeader() {
        return role == LEADER;
    }

    public Boolean isMember() {
        return role == MEMBER;
    }
}
