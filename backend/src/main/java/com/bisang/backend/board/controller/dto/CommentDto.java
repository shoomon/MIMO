package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;


public record CommentDto (
        Long commentId,
        Long userId,
        String userNickname,
        String userProfileImage,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
