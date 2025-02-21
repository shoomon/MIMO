package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.bisang.backend.team.domain.TeamNotificationStatus;

public record JoinTeamRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotBlank(message = "nickname 값은 필수입니다.")
        String nickname,
        @NotNull(message = "notificationStatus 값은 필수입니다.")
        TeamNotificationStatus notificationStatus
) {
}
