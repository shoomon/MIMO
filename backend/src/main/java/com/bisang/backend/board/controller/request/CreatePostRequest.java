package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePostRequest(
        @NotNull(message = "게시판 번호가 없습니다.")
        Long teamBoardId,
        @NotNull(message = "팀 번호가 없습니다.")
        Long teamId,
        @NotBlank(message = "제목을 입력해야 합니다.")
        String title,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String description,
        List<String> fileUris
) {
}
