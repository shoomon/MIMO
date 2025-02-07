package com.bisang.backend.board.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bisang.backend.board.controller.dto.CommentDto;

public record BoardDetailResponse(
    String boardType,
    String postTitle,
    String description,
    List<CommentDto> comments,
    LocalDateTime updatedAt
) {
}
