package com.bisang.backend.schedule.controller.request;

public record TeamScheduleCommentCreateRequest(
        Long teamId,
        Long teamScheduleId,
        Long teamUserId,
        Long parentCommentId,
        String content
) {
}
