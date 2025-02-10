package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTeamDescriptionRequest(
        @NotNull(message = "teamId 필드는 비어있으면 안됩니다.")
        Long teamId,
        @NotBlank(message = "description 필드는 비어있으면 안됩니다.")
        String description
) {
}
