package com.bisang.backend.board.controller.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotNull(message = "게시판 번호가 없습니다.")
        Long teamBoardId,
        @NotNull(message = "팀 유저 번호가 없습니다.")
        Long teamUserId,
        @NotBlank(message = "제목을 입력해야 합니다.")
        String title,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String description,
        List<String> fileUris
) {
}
