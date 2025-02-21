package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotNull;

public record LikePostRequest(
        @NotNull(message = "게시글 아이디가 없습니다.")
        Long boardId,
        @NotNull(message = "팀유저 아이디가 없습니다.")
        Long teamUserId
) {
}
