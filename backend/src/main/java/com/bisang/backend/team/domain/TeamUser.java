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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_user",
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_team_user", columnNames = {"team_id", "user_id"})
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

    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(name = "team_user_role", nullable = false)
    @Enumerated(STRING)
    private TeamUserRole role;

    @Column(name = "nofitication_status", nullable = false)
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
}
