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

    //TODO: 이거 온오프라인 처리할 때 필요할거임. 생성자에 넣고 처리해야함
    private Long userId;

    private Long teamUserId;

    private String chat;

    private LocalDateTime timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long teamUserId,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.teamUserId = teamUserId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

}
