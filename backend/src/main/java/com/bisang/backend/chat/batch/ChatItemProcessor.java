package com.bisang.backend.chat.batch;

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

        return ChatMessage.createMessage(
                redisChatMessage.getId(),
                redisChatMessage.getChatroomId(),
                redisChatMessage.getUserId(),
                redisChatMessage.getChat(),
                redisChatMessage.getTimestamp(),
                redisChatMessage.getType()
        );
    }
}
