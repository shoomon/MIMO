package com.bisang.backend.team.controller.request;

public record InviteRejectRequest(
    Long teamId,
    Long inviteId
) {
}
