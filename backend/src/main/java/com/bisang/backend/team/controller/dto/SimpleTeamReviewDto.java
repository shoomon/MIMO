package com.bisang.backend.team.controller.dto;

public record SimpleTeamReviewDto(
    Long teamId,
    Double reviewScore,
    Long reviewCount
) {
}
