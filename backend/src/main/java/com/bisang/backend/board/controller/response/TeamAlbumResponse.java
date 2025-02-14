package com.bisang.backend.board.controller.response;

import java.util.List;

import com.bisang.backend.board.controller.dto.AlbumImageDto;

public record TeamAlbumResponse(
        List<AlbumImageDto> images, //게시글 아이디, 이미지 uri
        Boolean hasNext
) {
}
