package com.bisang.backend.team.controller.response;

import java.util.List;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;

public record TeamTitleDescSearchResponse(
        Integer numOfTeams,
        Integer pageNumber,
        Integer size,
        List<SimpleTeamDto> teams
) {
}
