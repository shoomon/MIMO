package com.bisang.backend.schedule.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_schedule",
        indexes = {
            @Index(name = "idx_schedule_team_id", columnList = "team_id"),
        }
)
public class TeamSchedule extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_schedule_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(length = 100, name = "title", nullable = false)
    private String title;

    @Column(length = 100, name = "short_description", nullable = false)
    private String shortDescription;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "schedule_description_id", referencedColumnName = "schedule_description_id")
    private ScheduleDescription description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "max_participants", nullable = false)
    private Long maxParticipants;

    @Column(name = "current_participants", nullable = false)
    private Long currentParticipants;

    private TeamSchedule(
        Long teamId,
        Long teamUserId,
        String title,
        ScheduleDescription description,
        String location,
        LocalDateTime date,
        Long maxParticipants
    ) {
        this.teamId = teamId;
        this.teamUserId = teamUserId;
        this.title = title;
        this.shortDescription = description.getDescription().substring(100 - 3) + "...";
        this.description = description;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0L;
    }

    public void updateDescription(String description) {
        this.shortDescription = description.substring(100 - 3) + "...";
        this.description.updateDescription(description);
    }
}
