package com.bisang.backend.team.controller.request;

public record UpdateTeamNameRequest(
    Long teamId,
    String name
) {
}
