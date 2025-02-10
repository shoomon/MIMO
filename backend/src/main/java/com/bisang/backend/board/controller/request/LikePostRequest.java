package com.bisang.backend.board.controller.request;

public record LikePostRequest(
        Long boardId,
        Long userId
) {
}
