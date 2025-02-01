package com.bisang.backend.invite.domain;

import static com.bisang.backend.invite.domain.InviteStatus.WAITING;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_invite",
        indexes = {
                @Index(name = "idx_status_teamId_id", columnList = "invite_status, team_id, team_invite_id desc"),
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

    public TeamInvite createInviteRequest(Long teamId, Long userId, String memo) {
        return new TeamInvite(teamId, userId, WAITING, memo);
    }
}
