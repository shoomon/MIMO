package com.bisang.backend.team.controller.response;

import java.util.List;

import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.domain.TeamUserRole;

public record TeamUserResponse(
    Integer size,
    Boolean hasNext,
    TeamUserRole role,
    Long lastTeamUserId,
    List<TeamUserDto> users
) {
}
