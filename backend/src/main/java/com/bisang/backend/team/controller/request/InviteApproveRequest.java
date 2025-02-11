package com.bisang.backend.team.controller.request;

public record InviteApproveRequest(
    Long teamId,
    Long inviteId
) {
}
