package com.bisang.backend.schedule.controller.response;

import java.util.List;

import com.bisang.backend.schedule.controller.dto.TeamSimpleScheduleDto;

public record TeamSchedulesResponse(
        Integer size,
        Boolean hasNext,
        Long lastTeamScheduleId,
        List<TeamSimpleScheduleDto> schedules
) {
}
