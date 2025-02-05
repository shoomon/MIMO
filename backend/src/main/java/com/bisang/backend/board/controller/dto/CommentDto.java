package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record CommentDto(
        String title,
        String content,
        LocalDateTime writeDate
) {
}
