package com.bisang.backend.team.controller.dto;

import java.util.List;

import com.bisang.backend.team.domain.TeamUserRole;

public record SimpleTeamDto(
    Long teamId,
    Long teamUserId,
    TeamUserRole role,
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
