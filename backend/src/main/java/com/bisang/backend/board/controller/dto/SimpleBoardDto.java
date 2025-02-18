package com.bisang.backend.board.controller.dto;

import java.time.LocalDateTime;

public record SimpleBoardDto(
        BoardTeamDto boardTeamInfo,
        String imageUri
) {
}
