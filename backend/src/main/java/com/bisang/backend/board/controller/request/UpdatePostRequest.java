package com.bisang.backend.board.controller.request;

import java.util.List;

import com.bisang.backend.board.controller.dto.BoardFileDto;

public record UpdatePostRequest(
        String title,
        String description,
        List<BoardFileDto> filesToDelete,
        List<BoardFileDto> filesToAdd
) {
}
