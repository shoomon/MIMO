package com.bisang.backend.schedule.controller.request;

public record TeamScheduleCommentUpdateRequest(
        Long teamId,
        Long teamScheduleCommentId,
        String content
) {
}
