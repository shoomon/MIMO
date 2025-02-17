package com.bisang.backend.board.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.bisang.backend.board.controller.dto.CommentListDto;

public record BoardDetailResponse(
        BoardDto board,
        List<BoardFileDto> files,
        Boolean userLiked,
        List<CommentListDto> comments
) {
}
