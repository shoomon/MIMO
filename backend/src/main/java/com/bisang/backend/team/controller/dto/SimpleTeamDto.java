package com.bisang.backend.team.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SimpleTeamDto(
    Long teamId,
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
