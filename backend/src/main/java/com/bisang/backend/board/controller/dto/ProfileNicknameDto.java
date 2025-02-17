package com.bisang.backend.board.controller.dto;

public record ProfileNicknameDto(
        Long boardId,
        String profileUri,
        String nickname
) {
    public Long getBoardId() {
        return boardId;
    }
}
