package com.bisang.backend.schedule.controller.dto;

import java.time.LocalDateTime;

public record TeamSpecificScheduleDto(
        Long teamScheduleId,
        Long teamUserId,
        Double reviewScore,
        String title,
        String description,
        String location,
        Long price,
        LocalDateTime date,
        Long maxParticipants,
        Long currentParticipants
) {
}
