package com.bisang.backend.chat.controller.response;

import java.time.LocalDateTime;

import com.bisang.backend.chat.domain.ChatType;

public record ChatMessageResponse(
        Long id,
        Long teamUserId,
        String nickname,
        String profileImageUrl,
        String chat,
        LocalDateTime timestamp,
        ChatType chatType
) {
}
