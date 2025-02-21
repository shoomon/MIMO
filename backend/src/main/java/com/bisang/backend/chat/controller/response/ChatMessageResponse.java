package com.bisang.backend.chat.controller.response;

import java.time.LocalDateTime;

import com.bisang.backend.chat.domain.ChatType;

public record ChatMessageResponse(
        Long id,
        Long userId,
        String nickname,
        String profileImageUri,
        String chat,
        LocalDateTime timestamp,
        ChatType chatType
) {
}
