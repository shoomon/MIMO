package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BoardDto (
        Long postId,
        Long userId,
        String userProfileUri,
        String userNickname,
        String boardName,
        String postTitle,
        String description,
        Long likeCount,
        Long viewCount,
        LocalDateTime updatedAt
){
}
