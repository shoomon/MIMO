package com.bisang.backend.board.controller.response;

import com.bisang.backend.board.controller.dto.SimpleBoardListDto;

import java.util.List;

public record BoardListResponse(
        List<SimpleBoardListDto> boardList
) {
}
