package com.bisang.backend.team.controller.dto;

import java.util.List;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import com.bisang.backend.team.domain.TeamUserRole;

public record TeamDto(
        Long teamId,
        Long teamUserId,
        TeamUserRole role,
        String profileUri,
        String name,
        String description,
        TeamRecruitStatus recruitStatus,
        TeamPrivateStatus privateStatus,
        Area area,
        Long maxCapacity,
        Long currentCapacity,
        Double score,
        List<String> tags
) {
}
