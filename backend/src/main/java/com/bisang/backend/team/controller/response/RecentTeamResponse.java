package com.bisang.backend.team.controller.response;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;

import java.util.List;

public record RecentTeamResponse(
    List<SimpleTeamDto> teams
) {
}
