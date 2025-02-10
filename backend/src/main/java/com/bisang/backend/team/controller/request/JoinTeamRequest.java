package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.TeamNotificationStatus;

public record JoinTeamRequest(
    Long teamId,
    String nickname,
    TeamNotificationStatus notificationStatus
) {
}
