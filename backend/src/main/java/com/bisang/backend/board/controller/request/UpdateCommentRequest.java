package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @NotNull(message = "댓글 아이디가 없습니다.")
        Long commentId,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String content
) {
}
