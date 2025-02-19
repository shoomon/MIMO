package com.bisang.backend.team.controller.response;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;

import java.util.List;

public record TeamTagSearchResponse(
    Integer numberOfTeams,
    Integer pageNumber,
    Integer size,
    List<SimpleTeamDto> teams
) {
}
