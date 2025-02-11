package com.bisang.backend.invite.domain;

import static com.bisang.backend.invite.domain.InviteStatus.*;
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
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_invite",
        indexes = {
            @Index(name = "idx_team_status_id", columnList = "team_id, invite_status, team_invite_id"),
            @Index(name = "idx_team_id", columnList = "team_id, team_invite_id")},
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_team_user", columnNames = {"team_id", "user_id"})
        }
)
public class TeamInvite {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_invite_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(STRING)
    @Column(name = "invite_status", nullable = false)
    private InviteStatus status;

    @Column(length = 100, name = "memo", nullable = false)
    private String memo;

    private TeamInvite(Long teamId, Long userId, InviteStatus status, String memo) {
        this.teamId = teamId;
        this.userId = userId;
        this.status = status;
        this.memo = memo;
    }

    public static TeamInvite createInviteRequest(Long teamId, Long userId, String memo) {
        return new TeamInvite(teamId, userId, WAITING, memo);
    }

    public void approveInvitation() {
        status = ACCEPTED;
    }

    public void rejectInvitation() {
        status = REJECTED;
    }
}
