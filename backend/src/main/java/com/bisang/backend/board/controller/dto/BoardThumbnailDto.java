package com.bisang.backend.board.controller.dto;

public record BoardThumbnailDto(
        Long boardId,
        String imageUri
) {
    public Long getBoardId() {
        return boardId;
    }

    public String getImageUri() {
        return imageUri;
    }
}
