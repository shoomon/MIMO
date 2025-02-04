package com.bisang.backend.chat.domain.request;

import com.bisang.backend.chat.domain.ChatroomStatus;

public record ChatMessageRequest(
        Long teamUserId,
        String nickname,
        String teamName,
        ChatroomStatus chatroomStatus,
        String chat
) {
}