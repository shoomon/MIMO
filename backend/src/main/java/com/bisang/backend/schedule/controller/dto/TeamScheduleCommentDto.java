package com.bisang.backend.schedule.controller.dto;

import java.time.LocalDateTime;

public record TeamScheduleCommentDto(
        Long teamScheduleCommentId,
        Boolean isMyComment,
        String profileUri,
        String name,
        LocalDateTime time,
        Long commentSortId,
        Boolean hasParent,
        String content
) {
}
