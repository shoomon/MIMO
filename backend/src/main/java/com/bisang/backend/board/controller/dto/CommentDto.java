package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CommentDto(
        Long commentId,
        Long parentId,
        Long userId,
        String userNickname,
        String userProfileImage,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
