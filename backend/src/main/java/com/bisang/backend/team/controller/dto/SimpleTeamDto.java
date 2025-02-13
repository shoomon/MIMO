package com.bisang.backend.team.controller.dto;

import java.util.List;

public record SimpleTeamDto(
    Long teamId,
    Long teamUserId,
    String name,
    String description,
    String teamProfileUri,
    Double reviewScore,
    Long reviewCount,
    Long maxCapacity,
    Long currentCapacity,
    List<String> tags
) {
}
