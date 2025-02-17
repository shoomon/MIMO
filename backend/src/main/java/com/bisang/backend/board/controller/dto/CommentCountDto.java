package com.bisang.backend.board.controller.dto;

public record CommentCountDto(
        Long boardId,
        Long count
) {
    public Long getBoardId() {
        return boardId;
    }

    public Long getCount() {
        return count;
    }
}
