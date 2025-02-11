package com.bisang.backend.schedule.controller.dto;

import java.time.LocalDateTime;

import com.bisang.backend.schedule.domain.ScheduleStatus;

public record TeamSpecificScheduleDto(
        Long teamScheduleId,
        Long teamUserId,
        ScheduleStatus status,
        String nameOfLeader,
        String title,
        String description,
        String location,
        Long price,
        LocalDateTime date,
        Long maxParticipants,
        Long currentParticipants
) {
}
