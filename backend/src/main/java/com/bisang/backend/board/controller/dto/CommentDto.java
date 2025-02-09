package com.bisang.backend.board.controller.dto;

import com.bisang.backend.team.controller.dto.TeamUserDto;

import java.time.LocalDateTime;

public record CommentDto (
        Long id,
        String userId,
        String nickname,
        String userProfileImage,
        String content,
        LocalDateTime updatedAt
){
}
