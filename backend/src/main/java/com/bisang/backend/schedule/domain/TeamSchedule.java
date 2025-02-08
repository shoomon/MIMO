package com.bisang.backend.schedule.domain;

import static com.bisang.backend.schedule.domain.ScheduleStatus.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "max_participants", nullable = false)
    private Long maxParticipants;

    @Column(name = "current_participants", nullable = false)
    private Long currentParticipants;

    @Enumerated(STRING)
    @Column(name = "schedule_status", nullable = false)
    private ScheduleStatus scheduleStatus;

    @Builder
    public TeamSchedule(
        Long teamId,
        Long teamUserId,
        String title,
        String description,
        String location,
        Long price,
        LocalDateTime date,
        Long maxParticipants,
        String status
    ) {
        this.teamId = teamId;
        this.teamUserId = teamUserId;
        this.title = title;
        this.shortDescription = description.substring(100 - 3) + "...";
        this.description = description;
        this.location = location;
        this.price = price;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 1L;
        if (status.equals("A")) {
            this.scheduleStatus = AD_HOC;
            return;
        }
        this.scheduleStatus = REGURAL;
    }

    public void increaseCurrentParticipants() {
        currentParticipants++;
    }

    public void decreaseCurrentParticipants() {
        currentParticipants--;
    }

    public void updateMaxParticipants(Long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void updateDate(LocalDateTime date) {
        this.date = date;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    public void updateDescription(String newDescription) {
        this.shortDescription = newDescription.substring(100 - 3) + "...";
        this.description = newDescription;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void closeSchedule() {
        this.scheduleStatus = CLOSED;
    }
}
