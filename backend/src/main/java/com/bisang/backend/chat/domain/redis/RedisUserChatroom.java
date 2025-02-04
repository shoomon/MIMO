package com.bisang.backend.chat.domain.redis;

import com.bisang.backend.chat.domain.ChatroomStatus;

import java.io.Serializable;

public record RedisUserChatroom(
        Long teamId,
        String teamName,
        ChatroomStatus chatroomStatus
) implements Serializable {
}
