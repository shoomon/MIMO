package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record SimpleBoardListDto(
        Long boardId,
        String userProfileUri,
        String userNickname,
        String boardTitle,
        String imageUri,
        Long likeCount,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long commentCount
) {
    @JsonIgnore
    public Long getId() {
        return boardId;
    }
}
