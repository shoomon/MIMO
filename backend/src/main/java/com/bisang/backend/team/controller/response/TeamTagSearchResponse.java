package com.bisang.backend.team.controller.response;

import java.util.List;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;

public record TeamTagSearchResponse(
        Integer numberOfTags,
        Integer pageNumber,
        Integer size,
        List<SimpleTeamDto> teams
) {
}
