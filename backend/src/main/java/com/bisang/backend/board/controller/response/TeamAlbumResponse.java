package com.bisang.backend.board.controller.response;

import com.bisang.backend.board.controller.dto.BoardThumbnailDto;

import java.util.List;

public record TeamAlbumResponse(
        List<BoardThumbnailDto> images, //게시판 아이디, 게시글 아이디, 이미지 uri
        Boolean hasNext
) {
}
