package com.bisang.backend.team.controller.response;

import com.bisang.backend.team.controller.dto.SimpleTagDto;

import java.util.List;

public record TeamTagResponse(
    Integer numberOfTeams,
    Integer pageNumber,
    Integer size,
    List<SimpleTagDto> teams
) {
}
