package com.bisang.backend.team.controller.dto;

import java.util.List;

public record SimpleTeamDto(
    Long teamId,
    String name,
    String description,
    String teamProfileUri,
    Double reviewScore,
    Long maxCapacity,
    Long currentCapacity,
    List<String> tags
) {
}
