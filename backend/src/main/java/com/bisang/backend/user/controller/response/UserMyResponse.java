package com.bisang.backend.user.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.controller.dto.CommentDto;

import lombok.Builder;

@Builder
public record UserMyResponse(
        String name,
        String nickname,
        String email,
        String profileUri,
        Double reviewScore,
        Long mileage,
        Long mileageIncome,
        Long mileageOutcome,
        List<BoardDto> boards,
        List<CommentDto> comments
) {
}
