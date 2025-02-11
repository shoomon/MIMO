package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotNull;

public record LeaderDeleteUserRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotNull(message = "teamUserId 값은 필수입니다.")
        Long teamUserId
) {
}
