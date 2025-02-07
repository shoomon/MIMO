package com.bisang.backend.chat.domain.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.bisang.backend.chat.domain.ChatType;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RedisChatMessage implements Serializable {

    @Setter
    private Long id;

    private Long userId;

    private Long teamUserId;

    private String chat;

    private LocalDateTime timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long teamUserId,
            Long userId,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.teamUserId = teamUserId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

    public RedisChatMessage(
            Long id,
            Long teamUserId,
            Long userId,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.id = id;
        this.teamUserId = teamUserId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

}
