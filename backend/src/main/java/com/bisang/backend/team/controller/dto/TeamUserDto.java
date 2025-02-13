package com.bisang.backend.team.controller.dto;

import java.time.LocalDateTime;

import com.bisang.backend.team.domain.TeamUserRole;

public record TeamUserDto(
        Long userId,
        Long teamUserId,
        String nickname,
        String profileUri,
        TeamUserRole role,
        LocalDateTime joinTime
) {
}
