package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record BoardTeamDto(
        Long boardId,
        Long teamBoardId,
        String boardTitle,
        LocalDateTime boardCreatedAt,
        Long teamId,
        String teamName
) {
    @JsonIgnore
    public Long getBoardId() {
        return boardId;
    }
}