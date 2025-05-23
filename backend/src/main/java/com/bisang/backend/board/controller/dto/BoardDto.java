package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record BoardDto(
        Long postId,
        Long userId,
        Long teamUserId,
        String userProfileUri,
        String userNickname,
        String boardName,
        String postTitle,
        String description,
        Long likeCount,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
