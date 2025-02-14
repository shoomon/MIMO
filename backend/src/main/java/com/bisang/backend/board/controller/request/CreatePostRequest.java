package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public record CreatePostRequest(
        @NotNull(message = "게시판 아이디가 없습니다.")
        Long teamBoardId,
        @NotNull(message = "팀 아이디가 없습니다.")
        Long teamId,
        @NotBlank(message = "제목을 입력해야 합니다.")
        String title,
        @NotBlank(message = "내용을 입력해야 합니다.")
        String description,
        MultipartFile[] files
) {
}
