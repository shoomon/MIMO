package com.bisang.backend.schedule.controller.request;

import java.time.LocalDateTime;

public record TeamScheduleCreateRequest(
    Long teamId,
    Long teamUserId,
    String title,
    String description,
    String location,
    LocalDateTime date,
    Long maxParticipants
) {
}
