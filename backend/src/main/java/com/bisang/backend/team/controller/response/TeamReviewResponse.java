package com.bisang.backend.team.controller.response;

import java.util.List;

import com.bisang.backend.team.controller.dto.TeamReviewDto;

public record TeamReviewResponse(
    Integer size,
    Boolean hasNext,
    Long lastReviewId,
    List<TeamReviewDto> reviews
) {
}
