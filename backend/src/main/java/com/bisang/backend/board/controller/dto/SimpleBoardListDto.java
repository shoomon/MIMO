package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SimpleBoardListDto(
        Long postId,
        String userProfileUri,
        String userNickname,
        String postTitle,
        //todo: 게시글 미리보기 이미지 추가
        Long likeCount,
        Long viewCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime updatedAt,
        Long commentCount
) {
}
