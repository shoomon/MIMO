package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record CommentListDto(
        CommentDto rootComment,
        List<CommentDto> comments
) {
}
