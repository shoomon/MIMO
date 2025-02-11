package com.bisang.backend.board.controller.dto;

import jakarta.validation.constraints.NotNull;

public record BoardFileDto(
        Long fileId,
        String fileExtension,
        @NotNull
        String fileUri
) {
}
