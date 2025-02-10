package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotNull;

import com.bisang.backend.team.domain.TeamPrivateStatus;

public record UpdateTeamPrivateStatusRequest(
        @NotNull(message = "teamId 필드는 비어있으면 안됩니다.")
        Long teamId,
        @NotNull(message = "privateStatus 필드는 비어있으면 안됩니다.")
        TeamPrivateStatus privateStatus
) {
}
