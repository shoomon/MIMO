package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record SimpleCommentDto(
        BoardTeamDto boardTeamInfo,
        String commentContent,
        LocalDateTime createdAt,
        String imageUri

) {
}
