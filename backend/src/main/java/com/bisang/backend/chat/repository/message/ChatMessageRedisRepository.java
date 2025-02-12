package com.bisang.backend.chat.repository.message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageRedisRepository {

    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;

    // 채팅 메시지 ID를 auto_increment해줄 key
    private static final String messageIdKey = "chat:message:id";
    // 채팅 메시지를 저장하고 조회할 때 사용할 키
    private static final String teamMessageKey = "teamMessage:";

    public void saveMessage(long teamId, RedisChatMessage message) {
        Long messageId = redisChatMessageTemplate.opsForValue().increment(messageIdKey);
        long timestamp = message.getTimestamp().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();

        double score = timestamp + (messageId % 1000) / 1000.0;

        message.setId(messageId);

        redisChatMessageTemplate.opsForZSet().add(teamMessageKey + teamId, message, score);
    }

    public List<RedisChatMessage> getMessages(Long teamId, Long messageId, LocalDateTime timestamp) {
        String key = teamMessageKey + teamId;
        double score = timestamp.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() + (messageId % 1000) / 1000.0;

        Set<TypedTuple<RedisChatMessage>> result;

        if (messageId < 0) {
            // maxId가 0보다 작으면 모든 요소에서 내림차순으로 100개를 가져오기
            result = redisChatMessageTemplate.opsForZSet()
                    .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 30);
            return getRedisChatMessages(result);
        }

        // maxId가 0 이상이면, score보다 작은 값을 내림차순으로 100개 가져옴
        result = redisChatMessageTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, score, 1, 30);

        return getRedisChatMessages(result);
    }

    private static List<RedisChatMessage> getRedisChatMessages(
            Set<TypedTuple<RedisChatMessage>> result
    ) {
        if (result == null) {
            return Collections.emptyList();
        }

        return result.stream()
                .map(TypedTuple::getValue)
                .collect(Collectors.toList());
    }

    public RedisChatMessage getLastMessage(Long chatroomId) {
        Set<RedisChatMessage> result = redisChatMessageTemplate
                .opsForZSet()
                .reverseRange(teamMessageKey + chatroomId, 0, 0);

        if (result == null || result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    public Long unreadCount(Long chatroomId, Double lastReadScore) {
        return redisChatMessageTemplate.opsForZSet().count(teamMessageKey + chatroomId, lastReadScore + 0.0001, Double.MAX_VALUE);
    }
}
