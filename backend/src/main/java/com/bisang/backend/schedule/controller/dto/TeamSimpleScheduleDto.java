package com.bisang.backend.schedule.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TeamSimpleScheduleDto(
    Long teamScheduleId,
    LocalDateTime date,
    String title,
    Long price,
    List<ProfileDto> profileUris
) {
}
