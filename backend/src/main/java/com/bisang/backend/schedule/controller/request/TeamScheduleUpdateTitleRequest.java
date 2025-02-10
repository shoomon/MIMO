package com.bisang.backend.schedule.controller.request;

public record TeamScheduleUpdateTitleRequest(
    Long teamId,
    Long teamScheduleId,
    String title
) {
}
