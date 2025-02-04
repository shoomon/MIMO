package com.bisang.backend.team.controller.dto;

import com.bisang.backend.invite.domain.InviteStatus;

public record TeamInviteDto(
        Long teamInviteId,
        String name,
        String memo,
        InviteStatus status
) {
}
