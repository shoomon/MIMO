package com.bisang.backend.schedule.controller.request;

public record TeamScheduleUpdatePriceRequest(
    Long teamId,
    Long teamScheduleId,
    Long price
) {
}
