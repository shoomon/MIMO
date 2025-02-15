package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record SimpleBoardDto(
        String boardTitle,
        String teamName,
        LocalDateTime createdAt,
        String imageUri,
        Long teamId,
        Long teamBoardId,
        Long boardId
) {
}
