package com.bisang.backend.board.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePostRequest(
        @NotNull
        Long teamBoardId,
        @NotNull
        Long teamId,
        @NotNull
        String title,
        @NotNull
        String description,
        List<String> fileUris
) {
}
