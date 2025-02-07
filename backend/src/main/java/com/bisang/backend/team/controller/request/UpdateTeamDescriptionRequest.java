package com.bisang.backend.team.controller.request;

public record UpdateTeamDescriptionRequest(
    Long teamId,
    String description
) {
}
