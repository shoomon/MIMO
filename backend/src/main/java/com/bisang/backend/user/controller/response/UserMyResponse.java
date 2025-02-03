package com.bisang.backend.user.controller.response;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.controller.dto.CommentDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

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
        List<BoardDto>boards,
        List<CommentDto> comments
) {
}
