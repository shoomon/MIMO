package com.bisang.backend.user.controller.response;

import com.bisang.backend.board.controller.dto.SimpleCommentDto;

import java.util.List;

public record UserCommentResponse(
        List<SimpleCommentDto> userComment
) {
}
