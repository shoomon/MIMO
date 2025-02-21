package com.bisang.backend.user.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.SimpleBoardDto;
import com.bisang.backend.board.controller.dto.SimpleCommentDto;

import lombok.Builder;

@Builder
public record UserMyResponse(
        String name,
        String nickname,
        String email,
        String profileUri,
        String accountNumber,
        Double reviewScore,
        Long mileage,
        Long mileageIncome,
        Long mileageOutcome,
        List<SimpleBoardDto> userBoard,
        List<SimpleCommentDto> userComment
) {
}
