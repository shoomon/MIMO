package com.bisang.backend.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record BoardThumbnailDto(
        Long teamBoardId,
        Long boardId,
        String imageUri
) {
    @JsonIgnore
    public Long getBoardId() {
        return boardId;
    }

    @JsonIgnore
    public String getImageUri() {
        return imageUri;
    }
}
