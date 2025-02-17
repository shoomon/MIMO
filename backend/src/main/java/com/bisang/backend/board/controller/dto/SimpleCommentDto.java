package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record SimpleCommentDto(
        Long userId,
        String title,
        String content,
        LocalDateTime writeDate
) {
}
