package com.bisang.backend.chat.domain.response;

import com.bisang.backend.chat.domain.ChatType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long teamUserId,
        String nickname,
        String profileImageUrl,
        String chat,
        LocalDateTime timestamp,
        ChatType chatType
) {
}
