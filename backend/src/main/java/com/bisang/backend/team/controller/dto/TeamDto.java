package com.bisang.backend.team.controller.dto;

import java.util.List;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import lombok.Builder;

@Builder
public record TeamDto(
        Long teamId,
        String profileUri,
        String name,
        String description,
        String accountNumber,
        TeamRecruitStatus recruitStatus,
        TeamPrivateStatus privateStatus,
        Long maxCapacity,
        Long currentCapacity,
        Double reviewScore,
        Long reviewCount,
        List<String> tags
) {
}
