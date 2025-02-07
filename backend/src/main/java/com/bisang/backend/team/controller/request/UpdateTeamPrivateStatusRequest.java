package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.TeamPrivateStatus;

public record UpdateTeamPrivateStatusRequest(
    Long teamId,
    TeamPrivateStatus status
) {
}
