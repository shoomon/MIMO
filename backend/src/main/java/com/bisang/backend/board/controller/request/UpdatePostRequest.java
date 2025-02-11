package com.bisang.backend.board.controller.request;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdatePostRequest(
        String title,
        String description,
        List<BoardFileDto> filesToDelete,
        List<BoardFileDto> filesToAdd
) {
}
