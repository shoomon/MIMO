package com.bisang.backend.schedule.controller.request;

import java.time.LocalDateTime;

public record TeamScheduleUpdateDateRequest(
    Long teamId,
    Long teamScheduleId,
    LocalDateTime date
) {
}
