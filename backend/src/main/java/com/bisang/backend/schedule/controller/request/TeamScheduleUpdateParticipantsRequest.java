package com.bisang.backend.schedule.controller.request;

public record TeamScheduleUpdateParticipantsRequest(
    Long teamId,
    Long teamScheduleId,
    Long maxParticipants
) {
}
