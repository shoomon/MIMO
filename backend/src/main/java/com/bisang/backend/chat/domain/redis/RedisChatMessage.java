package com.bisang.backend.chat.domain.redis;

import java.io.Serializable;

import com.bisang.backend.chat.domain.ChatType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RedisChatMessage implements Serializable {

    @Setter
    private Long id;

    private Long chatroomId;

    private Long userId;

    private String chat;

    private Long timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long chatroomId,
            Long userId,
            String chat,
            Long timestamp,
            ChatType type
    ) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

    public RedisChatMessage(
            Long id,
            Long chatroomId,
            Long userId,
            String chat,
            Long timestamp,
            ChatType type
    ) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }
}
