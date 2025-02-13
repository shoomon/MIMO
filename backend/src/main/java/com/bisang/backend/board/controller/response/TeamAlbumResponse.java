package com.bisang.backend.board.controller.response;

import com.bisang.backend.board.controller.dto.AlbumImageDto;

import java.util.List;

public record TeamAlbumResponse(
        List<AlbumImageDto> images,//게시글 아이디, 이미지 uri
        Boolean hasNext
) {
}
