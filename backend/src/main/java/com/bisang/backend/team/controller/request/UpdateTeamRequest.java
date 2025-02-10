package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTeamRequest(
        @NotNull(message = "teamId 필드는 비어있으면 안됩니다.")
        Long teamId,
        @NotBlank(message = "name 필드는 비어있으면 안됩니다.")
        String name,
        @NotBlank(message = "description 필드는 비어있으면 안됩니다.")
        String description,
        @NotNull(message = "recruitStatus 필드는 비어있으면 안됩니다.")
        TeamRecruitStatus recruitStatus,
        @NotNull(message = "privateStatus 필드는 비어있으면 안됩니다.")
        TeamPrivateStatus privateStatus,
        @NotBlank(message = "profileUri 필드는 비어있으면 안됩니다.")
        String profileUri,
        @NotNull(message = "area 필드는 비어있으면 안됩니다.")
        Area area
) {
}
