package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.Area;

public record UpdateTeamAreaRequest(
    Long teamId,
    Area area
) {
}
