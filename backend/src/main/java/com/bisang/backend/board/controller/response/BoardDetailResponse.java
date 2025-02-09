package com.bisang.backend.board.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.controller.dto.CommentDto;

public record BoardDetailResponse(
    BoardDto board,
    List<CommentDto> comments
) {
}
