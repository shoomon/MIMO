package com.bisang.backend.invite.controller.response;

import java.util.List;

import com.bisang.backend.invite.controller.dto.TeamInviteDto;

public record TeamInvitesResponse(
    Integer size,
    Boolean hasNext,
    Long lastTeamInviteId,
    List<TeamInviteDto> teamInvites
) {
}
