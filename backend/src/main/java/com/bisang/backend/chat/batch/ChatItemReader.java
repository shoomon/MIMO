package com.bisang.backend.chat.batch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;

@Component
public class ChatItemReader implements ItemReader<RedisChatMessage> {

    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;
    private int index = 0;
    private List<RedisChatMessage> oldMessages = new ArrayList<>();

    public ChatItemReader(RedisTemplate<String, RedisChatMessage> redisTemplate) {
        this.redisChatMessageTemplate = redisTemplate;
    }

    @Override
    public RedisChatMessage read() {
        if (oldMessages.isEmpty()) {
            oldMessages = fetchOldChatMessages();
        }

        if (index < oldMessages.size()) {
            return oldMessages.get(index++);
        }
        return null;
    }

    private List<RedisChatMessage> fetchOldChatMessages() {
        List<RedisChatMessage> result = new ArrayList<>();
        Set<String> chatRooms = redisChatMessageTemplate.keys("teamMessage:*");

        //7일 전 구하기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        ZonedDateTime zonedDateTime = sevenDaysAgo.atZone(ZoneId.systemDefault());
        double epochMilli = zonedDateTime.toInstant().toEpochMilli();

        for (String roomKey : chatRooms) {
            Set<TypedTuple<RedisChatMessage>> messages = redisChatMessageTemplate
                    .opsForZSet()
                    .rangeByScoreWithScores(roomKey, 0, epochMilli);

            getResult(messages, result);
        }
        return result;
    }

    private static void getResult(
            Set<TypedTuple<RedisChatMessage>> messages,
            List<RedisChatMessage> result
    ) {
        if (messages == null) {
            return;
        }

        for (TypedTuple<RedisChatMessage> message : messages) {
            RedisChatMessage parts = message.getValue();
            result.add(parts);
        }
    }
}
