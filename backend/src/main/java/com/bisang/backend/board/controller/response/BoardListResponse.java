package com.bisang.backend.board.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.SimpleBoardListDto;

public record BoardListResponse(
        List<SimpleBoardListDto> boardList
) {
}
