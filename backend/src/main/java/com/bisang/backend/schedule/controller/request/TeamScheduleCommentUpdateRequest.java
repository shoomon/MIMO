package com.bisang.backend.schedule.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamScheduleCommentUpdateRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotNull(message = "commentId 값은 필수입니다.")
        Long teamScheduleCommentId,
        @NotBlank(message = "댓글 내용 값은 필수입니다.")
        String content
) {
}
