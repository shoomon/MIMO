package com.bisang.backend.invite.controller.dto;

import com.bisang.backend.invite.domain.InviteStatus;

public record TeamInviteDto(
    Long teamInviteId,
    Long teamId,
    Long userId,
    String profileUri,
    InviteStatus status,
    String name,
    String memo
) {
}
