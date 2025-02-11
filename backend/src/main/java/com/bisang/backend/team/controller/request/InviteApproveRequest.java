package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotNull;

public record InviteApproveRequest(
        @NotNull(message = "teamId는 누락될 수 없습니다.")
        Long teamId,
        @NotNull(message = "inviteId는 누락될 수 없습니다.")
        Long inviteId
) {
}
