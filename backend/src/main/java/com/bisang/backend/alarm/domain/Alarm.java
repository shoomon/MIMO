package com.bisang.backend.alarm.domain;

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
    name = "alarm",
    indexes = {
        @Index(name = "idx_alarm_user_id", columnList = "user_id")
    }
)
public class Alarm {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    public Alarm(Long userId, Long scheduleId, String title, String description) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.description = description;
    }
}
