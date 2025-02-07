package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record BoardDto(
        String title,
        String teamName,
        LocalDateTime writeDate
) {
}
