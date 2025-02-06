package com.bisang.backend.chat.controller.request;

import com.bisang.backend.chat.domain.ChatroomStatus;

public record ChatMessageRequest(
        Long teamUserId,
        String chat
) {
}