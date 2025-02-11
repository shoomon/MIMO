package com.bisang.backend.chat.controller.request;

public record ChatMessageRequest(
        Long teamUserId,
        String chat
) {
}