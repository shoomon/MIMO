package com.bisang.backend.board.controller.response;

import com.bisang.backend.board.controller.dto.TeamBoardDto;

import java.util.List;

public record TeamBoardListResponse(
        List<TeamBoardDto> teamBoardList
) {
}
