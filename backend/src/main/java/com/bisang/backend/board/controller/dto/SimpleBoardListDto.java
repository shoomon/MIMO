package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record SimpleBoardListDto(
        Long postId,
        String userProfileUri,
        String userNickname,
        String postTitle,
        String imageUri,
        Long likeCount,
        Long viewCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime createdAt, //board
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime updatedAt, //board
        Long commentCount //comment
) {
    @JsonIgnore
    public Long getId() {
        return postId;
    }
}
