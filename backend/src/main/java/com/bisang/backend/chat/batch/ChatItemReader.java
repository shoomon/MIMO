package com.bisang.backend.chat.batch;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@StepScope
public class ChatItemReader implements ItemReader<RedisChatMessage> {

    private final StringRedisTemplate redisTemplate;

    private List<RedisChatMessage> oldMessages = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private int index = 0;

    public ChatItemReader(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
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
        Set<String> chatRooms = redisTemplate.keys("teamMessage:*");

        //7일 전 구하기
        double epochMilli = Instant.now().minus(7, ChronoUnit.DAYS).toEpochMilli();

        for (String roomKey : chatRooms) {
            Set<TypedTuple<String>> messages = redisTemplate
                    .opsForZSet()
                    .rangeByScoreWithScores(roomKey, 0, epochMilli);

            result = getRedisChatMessages(messages);
        }
        return result;
    }

    private List<RedisChatMessage> getRedisChatMessages(
            Set<TypedTuple<String>> result
    ) {
        if (result == null) {
            return Collections.emptyList();
        }

        List<String> list = result.stream()
                .map(TypedTuple::getValue)
                .toList();

        return list.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, RedisChatMessage.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to deserialize message", e);
                    }
                })
                .collect(Collectors.toList());
    }
}
