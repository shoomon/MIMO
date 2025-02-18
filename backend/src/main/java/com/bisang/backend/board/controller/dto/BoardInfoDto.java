package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record BoardInfoDto(
        Long postId,
        Long teamId,
        Long userId,
        Long teamUserId,
        String boardName,
        String postTitle,
        String description,
        Long likeCount,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
