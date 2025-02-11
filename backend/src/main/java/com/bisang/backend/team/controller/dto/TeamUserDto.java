package com.bisang.backend.team.controller.dto;

import com.bisang.backend.team.domain.TeamUserRole;

public record TeamUserDto(
        Long teamUserId,
        String nickname,
        TeamUserRole role
) {
}
