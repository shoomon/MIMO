package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotNull;

import com.bisang.backend.team.domain.TeamRecruitStatus;

public record UpdateTeamRecruitStatusRequest(
        @NotNull(message = "teamId 필드는 비어있으면 안됩니다.")
        Long teamId,
        @NotNull(message = "recruitStatus 필드는 비어있으면 안됩니다.")
        TeamRecruitStatus recruitStatus
) {
}
