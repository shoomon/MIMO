package com.bisang.backend.chat.domain.redis;

import com.bisang.backend.chat.domain.ChatType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class RedisChatMessage implements Serializable {

    @Setter
    private Long id;

    private Long userId;

    private String nickname;

    private String chat;

    private LocalDateTime timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long userId,
            String nickname,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

}
