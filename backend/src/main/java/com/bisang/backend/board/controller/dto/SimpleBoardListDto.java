package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SimpleBoardListDto(
        Long userId, //이것도 팀유저 줘야되나
        Long userNickname,
        Long userProfileUri,
        String title,
        String description,
        Long commentCount,
        Long likeCount,
        Long viewCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
        LocalDateTime updatedAt
) {
}
