package com.bisang.backend.team.controller.request;

public record InviteTeamRequest(
    Long teamId,
    String memo
) {
}
