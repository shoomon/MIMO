package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotNull;

public record TeamAlbumRequest(
        @NotNull(message = "팀 아이디가 없습니다.")
        Long teamId,
        Long lastReadImageId
) {
}
