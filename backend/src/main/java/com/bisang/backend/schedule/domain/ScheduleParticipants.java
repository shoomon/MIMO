package com.bisang.backend.schedule.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "schedule_participants",
        indexes = {
            @Index(name = "idx_schedule_participants_team_schedule_id",
                    columnList = "team_schedule_id, team_user_id"),
        }
)
public class ScheduleParticipants {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_participants_id")
    private Long id;

    @Column(name = "team_schedule_id", nullable = false)
    private Long teamScheduleId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;
}
