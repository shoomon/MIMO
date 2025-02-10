package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotNull;

import com.bisang.backend.team.domain.Area;

public record UpdateTeamAreaRequest(
        @NotNull(message = "teamId 필드는 비어있으면 안됩니다.")
        Long teamId,
        @NotNull(message = "area 필드는 비어있으면 안됩니다.")
        Area area
) {
}
