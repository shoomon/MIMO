package com.bisang.backend.board.controller.dto;

import com.bisang.backend.team.controller.dto.TeamUserDto;

import java.time.LocalDateTime;

public record CommentDto (
        Long commentId,
        Long userId,
        String userNickname,
        String userProfileImage,
        String content,
        LocalDateTime updatedAt
){
}
