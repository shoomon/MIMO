package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record SimpleBoardDto(
        Long boardId,
        String title,
        String teamName,
        LocalDateTime writeDate
) {
}
