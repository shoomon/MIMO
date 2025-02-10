package com.bisang.backend.schedule.controller.request;

public record TeamScheduleCommentDeleteRequest(
        Long teamId,
        Long teamScheduleCommentId
) {
}
