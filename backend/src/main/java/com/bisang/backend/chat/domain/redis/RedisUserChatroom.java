package com.bisang.backend.chat.domain.redis;

import java.io.Serializable;

public record RedisUserChatroom(
        Long teamId,
        String teamName
) implements Serializable {
}
