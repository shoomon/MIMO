package com.bisang.backend.board.controller.request;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdatePostRequest(
        @NotNull(message = "개시글 번호가 없습니다.")
        Long postId,
        @NotBlank(message = "제목을 입력해야 합니다.")
        String title,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String description,
        List<BoardFileDto> filesToDelete,
        List<BoardFileDto> filesToAdd
) {
}
