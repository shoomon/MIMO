package com.bisang.backend.board.controller.dto;

import java.util.List;

public record TeamBoardDto(
        Long teamBoardId,
        String teamBoardName,
        List<SimpleBoardListDto> boardList
) {
}
