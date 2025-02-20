package com.bisang.backend.chat.repository.message;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.common.exception.ChatroomException;
import com.bisang.backend.common.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 채팅 메시지를 저장하고 조회할 때 사용할 키
    private static final String teamMessageKey = "teamMessage:";

    public List<RedisChatMessage> getMessages(Long teamId, Long messageId, String timestamp, Double teamEnterScore) {

        String key = teamMessageKey + teamId;
        Set<TypedTuple<String>> result;

        if (messageId < 0) {
            result = redisTemplate.opsForZSet()
                    .reverseRangeByScoreWithScores(key, teamEnterScore, Double.POSITIVE_INFINITY, 0, 30);

            return getRedisChatMessages(result);
        }

        LocalDateTime datetime;
        try {
            datetime = DateUtils.dateToLocalDateTime(timestamp);
        } catch (DateTimeParseException e) {
            throw new ChatroomException(INVALID_REQUEST);
        }

        double score = datetime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() + (messageId % 1000) / 1000.0;
        result = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, teamEnterScore, score, 1, 30);
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

        String json = result.iterator().next();
        try {
            return objectMapper.readValue(json, RedisChatMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Json 파싱 실패: {}", e.getMessage());
            return null;
        }
    }

    public Long unreadCount(Long chatroomId, Double lastReadScore) {
        return redisTemplate
                .opsForZSet()
                .count(teamMessageKey + chatroomId, lastReadScore, Double.MAX_VALUE);
    }

    public boolean checkChat(Long chatroomId, Double lastReadScore) {
        Long count = redisTemplate.opsForZSet()
                .count(teamMessageKey + chatroomId, lastReadScore, lastReadScore); // 특정 score 값만 조회
        return count != null && count > 0;
    }

    public Long countAllChat(Long chatroomId) {
        return redisTemplate.opsForZSet().size(teamMessageKey + chatroomId);
    }
}
