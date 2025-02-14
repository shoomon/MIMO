package com.bisang.backend.board.controller.dto;

import java.util.List;

public record CommentListDto(
        CommentDto rootComment,
        List<CommentDto> comments
) {
}
