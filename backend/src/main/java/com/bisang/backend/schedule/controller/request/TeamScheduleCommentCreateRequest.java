package com.bisang.backend.schedule.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamScheduleCommentCreateRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotNull(message = "teamScheduleId 값은 필수입니다.")
        Long teamScheduleId,
        @NotNull(message = "teamUserId 값은 필수입니다.")
        Long teamUserId,
        Long parentCommentId,
        @NotBlank(message = "댓글 내용 값은 필수입니다.")
        String content
) {
}
