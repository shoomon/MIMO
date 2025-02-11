package com.bisang.backend.schedule.domain;

import static com.bisang.backend.schedule.domain.ParticipantsRole.CREATOR;
import static com.bisang.backend.schedule.domain.ParticipantsRole.PARTICIPANTS;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
        name = "schedule_participants",
        indexes = {
            @Index(name = "idx_schedule_participants_team_schedule_id",
                    columnList = "team_schedule_id, team_user_id")
        }
)
public class ScheduleParticipants {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_participants_id")
    private Long id;

    @Column(name = "team_schedule_id", nullable = false)
    private Long teamScheduleId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(name = "user_role", nullable = false)
    private ParticipantsRole userRole;

    private ScheduleParticipants(Long teamScheduleId, Long userId, Long teamUserId, ParticipantsRole userRole) {
        this.teamScheduleId = teamScheduleId;
        this.userId = userId;
        this.teamUserId = teamUserId;
        this.userRole = userRole;
    }

    public static ScheduleParticipants creator(Long teamScheduleId, Long userId, Long teamUserId) {
        return new ScheduleParticipants(teamScheduleId, userId, teamUserId, CREATOR);
    }

    public static ScheduleParticipants participants(Long teamScheduleId, Long userId, Long teamUserId) {
        return new ScheduleParticipants(teamScheduleId, userId, teamUserId, PARTICIPANTS);
    }
}
