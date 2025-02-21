package com.bisang.backend.board.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.TeamBoardDto;

public record TeamBoardListResponse(
        List<TeamBoardDto> teamBoardList
) {
}
