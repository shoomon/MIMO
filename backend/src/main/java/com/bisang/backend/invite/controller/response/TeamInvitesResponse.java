package com.bisang.backend.invite.controller.response;

import com.bisang.backend.invite.controller.dto.TeamInviteDto;

import java.util.List;

public record TeamInvitesResponse(
    Integer size,
    Boolean hasNext,
    Long lastTeamInviteId,
    List<TeamInviteDto> teamInvites
) {
}
