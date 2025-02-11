package com.bisang.backend.team.controller.response;

import java.util.List;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;

public record TeamInfosResponse(
    Integer size,
    Boolean hasNext,
    Long lastTeamId,
    List<SimpleTeamDto> teams
) {
}
