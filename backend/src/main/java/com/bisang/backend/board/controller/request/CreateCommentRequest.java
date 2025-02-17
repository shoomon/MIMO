package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotNull(message = "팀 유저 아이디가 없습니다.")
        Long teamUserId,
        @NotNull(message = "게시글 아이디가 없습니다.")
        Long postId,
        Long parentId,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String content
) {
}
