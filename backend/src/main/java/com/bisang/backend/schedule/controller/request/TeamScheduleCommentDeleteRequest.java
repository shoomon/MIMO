package com.bisang.backend.schedule.controller.request;

import jakarta.validation.constraints.NotNull;

public record TeamScheduleCommentDeleteRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotNull(message = "commentId 값은 필수입니다.")
        Long teamScheduleCommentId
) {
}
