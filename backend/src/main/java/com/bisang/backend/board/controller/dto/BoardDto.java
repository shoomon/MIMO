package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record BoardDto (
        Long postId,
        Long userId,
        String userProfileUri,
        String userNickname,
        String boardName,
        String postTitle,
        String description,
        LocalDateTime updatedAt
){
}
