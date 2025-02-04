package com.bisang.backend.chat.domain.request;

public record ChatMessageRequest(
        Long teamUserId,
        String nickname,
        String teamName,
        String chat
) {
}