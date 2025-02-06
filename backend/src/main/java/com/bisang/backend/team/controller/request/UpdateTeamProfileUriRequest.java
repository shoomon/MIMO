package com.bisang.backend.team.controller.request;

public record UpdateTeamProfileUriRequest(
    Long teamId,
    String profileUri
) {
}
