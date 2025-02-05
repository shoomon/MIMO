package com.bisang.backend.team.controller.response;

import java.time.LocalDate;

import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamUserRole;

import lombok.Builder;

@Builder
public record SingleTeamUserInfoResponse(
    Long teamUserId,
    String nickname,
    TeamUserRole role,
    TeamNotificationStatus status,
    LocalDate joinDate
) {
}
