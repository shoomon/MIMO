package com.bisang.backend.team.controller.dto;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record SpecificTeamDto(
        Long teamId,
        String profileUri,
        String name,
        String description,
        String accountNumber,
        TeamRecruitStatus recruitStatus,
        TeamPrivateStatus privateStatus,
        Area area,
        TeamCategory category,
        Long maxCapacity,
        Long currentCapacity,
        Double reviewScore,
        Long reviewCount,
        List<String> tags
) {
}
