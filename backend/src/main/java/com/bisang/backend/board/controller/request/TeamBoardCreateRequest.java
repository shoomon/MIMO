package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamBoardCreateRequest(
        @NotNull(message = "팀 아이디가 없습니다.")
        Long teamId,
        @NotBlank(message = "게시판 이름이 없습니다.")
        String teamBoardName
) {
}
