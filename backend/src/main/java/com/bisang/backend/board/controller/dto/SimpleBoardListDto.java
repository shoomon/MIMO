package com.bisang.backend.board.controller.dto;

public record SimpleBoardListDto(
        BoardDto board,
        Long commentCount
) {
}
