package com.bisang.backend.schedule.controller.request;

public record TeamScheduleUpdateDescriptionRequest(
    Long teamId,
    Long teamScheduleId,
    String description
) {
}
