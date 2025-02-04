package com.bisang.backend.chat.domain.redis;

import com.bisang.backend.chat.domain.ChatType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class RedisChatMessage implements Serializable {

    @Setter
    private Long id;

    private Long teamUserId;

    private String nickname;

    private String chat;

    private LocalDateTime timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long teamUserId,
            String nickname,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.teamUserId = teamUserId;
        this.nickname = nickname;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

}
