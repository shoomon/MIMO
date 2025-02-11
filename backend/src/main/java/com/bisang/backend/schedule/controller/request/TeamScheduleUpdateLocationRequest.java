package com.bisang.backend.schedule.controller.request;

public record TeamScheduleUpdateLocationRequest(
    Long teamId,
    Long teamScheduleId,
    String location
) {
}
