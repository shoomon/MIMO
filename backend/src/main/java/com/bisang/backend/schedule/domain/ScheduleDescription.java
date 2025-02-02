package com.bisang.backend.schedule.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
        name = "schedule_description"
)
public class ScheduleDescription {
    @Id
    @Column(name = "schedule_description_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String description;

    public ScheduleDescription(String description) {
        this.description = description;
    }
}
