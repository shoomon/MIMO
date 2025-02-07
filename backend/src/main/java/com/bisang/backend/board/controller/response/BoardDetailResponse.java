package com.bisang.backend.board.controller.response;

import com.bisang.backend.board.controller.dto.CommentDto;

import java.util.List;

public record BoardDetailResponse(
    String boardType,
    String postTitle,
    String description,
    List<CommentDto> comments
){}
