package com.bisang.backend.schedule.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bisang.backend.schedule.domain.ScheduleStatus;

import lombok.Builder;

@Builder
public record TeamScheduleSpecificResponse(
        Long teamScheduleId,
        ScheduleStatus status,
        String location,
        LocalDateTime date,
        Long price,
        String nameOfLeader,
        List<String> profileUris,
        Long maxParticipants,
        Long currentParticipants,
        String title,
        String description
) {
}
