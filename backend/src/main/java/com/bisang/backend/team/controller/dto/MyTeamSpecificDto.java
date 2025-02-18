package com.bisang.backend.team.controller.dto;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;

import java.util.List;

public record MyTeamSpecificDto(
        Long teamId,
        String profileUri,
        String name,
        String description,
        TeamRecruitStatus recruitStatus,
        TeamPrivateStatus privateStatus,
        String area,
        String category,
        Long maxCapacity,
        Long currentCapacity,
        Double reviewScore,
        Long reviewCount,
        List<String> tags
) {
}
