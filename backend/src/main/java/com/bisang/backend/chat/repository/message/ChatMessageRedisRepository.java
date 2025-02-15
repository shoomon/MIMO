package com.bisang.backend.chat.repository.message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.common.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageRedisRepository {

    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 채팅 메시지를 저장하고 조회할 때 사용할 키
    private static final String teamMessageKey = "teamMessage:";

    public List<RedisChatMessage> getMessages(Long teamId, Long messageId, String timestamp) {

        String key = teamMessageKey + teamId;
        Set<TypedTuple<String>> result;

        if (messageId < 0) {
            result = redisTemplate.opsForZSet()
                    .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 30);

            return getRedisChatMessages(result);
        }

        LocalDateTime datetime = DateUtils.DateToLocalDateTime(timestamp);
        double score = datetime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() + (messageId % 1000) / 1000.0;
        result = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, score, 1, 30);
        return getRedisChatMessages(result);
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

    public RedisChatMessage getLastMessage(Long chatroomId) {
        Set<String> result = redisTemplate
                .opsForZSet()
                .reverseRange(teamMessageKey + chatroomId, 0, 0);

        if (result == null || result.isEmpty()) {
            return null;
        }

        List<RedisChatMessage> messages = result.stream()
            .map(json -> {
                try {
                    return objectMapper.readValue(json, RedisChatMessage.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();

        return messages.isEmpty() ? null : messages.get(0);
    }

    public Long unreadCount(Long chatroomId, Double lastReadScore) {
        return redisChatMessageTemplate
                .opsForZSet()
                .count(teamMessageKey + chatroomId, lastReadScore, Double.MAX_VALUE);
    }

    public boolean checkChat(Long chatroomId, Double lastReadScore) {
        Long count = redisChatMessageTemplate.opsForZSet()
                .count(teamMessageKey + chatroomId, lastReadScore, lastReadScore); // 특정 score 값만 조회
        return count != null && count > 0;
    }

    public Long countAllChat(Long chatroomId) {
        return redisChatMessageTemplate.opsForZSet().size(teamMessageKey + chatroomId);
    }
}
