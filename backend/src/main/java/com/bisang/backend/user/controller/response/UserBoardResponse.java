package com.bisang.backend.user.controller.response;

import com.bisang.backend.board.controller.dto.SimpleBoardDto;

import java.util.List;

public record UserBoardResponse(
        List<SimpleBoardDto> userBoard
) {
}
