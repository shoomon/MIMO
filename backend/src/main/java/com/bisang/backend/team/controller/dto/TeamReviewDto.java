package com.bisang.backend.team.controller.dto;

import java.time.LocalDateTime;

public record TeamReviewDto(
    Long teamReviewId,
    String memo,
    Long score,
    LocalDateTime createAt
) {
}
