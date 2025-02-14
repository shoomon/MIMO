package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
//
public record SimpleBoardListDto(
        Long postId, //board
        String userProfileUri, //user
        String userNickname, //teamUser
        String postTitle, //board
        String imageUri, //teamboardImage
        Long likeCount, //board
        Long viewCount, //board
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime createdAt, //board
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime updatedAt, //board
        Long commentCount //comment
) {
        public Long getId() {
                return postId;
        }
}
