package com.bisang.backend.chat.batch;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;

import io.micrometer.common.lang.Nullable;

@Component
public class ChatItemProcessor implements ItemProcessor<RedisChatMessage, ChatMessage> {

    @Override
    public ChatMessage process(@Nullable RedisChatMessage redisChatMessage) {
        if (redisChatMessage == null) {
            return null;
        }

        Long timestamp = redisChatMessage.getTimestamp();
        LocalDateTime localTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));

        return ChatMessage.createMessage(
                redisChatMessage.getId(),
                redisChatMessage.getChatroomId(),
                redisChatMessage.getUserId(),
                redisChatMessage.getChat(),
                localTimestamp,
                redisChatMessage.getType()
        );
    }
}
