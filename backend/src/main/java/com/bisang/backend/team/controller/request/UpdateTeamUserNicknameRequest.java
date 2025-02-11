package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTeamUserNicknameRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotBlank(message = "nickname 값은 필수입니다.")
        String nickname
) {
}
