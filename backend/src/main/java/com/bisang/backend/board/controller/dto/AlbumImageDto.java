package com.bisang.backend.board.controller.dto;

public record AlbumImageDto(
        Long imageId,
        Long postId,
        String imageUri
) {
}
