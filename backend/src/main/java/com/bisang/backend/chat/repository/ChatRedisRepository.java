package com.bisang.backend.chat.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;
    private final RedisTemplate<String, Long> redisLongTemplate;

    private static final String messageIdKey = "chat:message:id"; // 채팅 메시지 ID를 관리할 key

    public void insertMember(Long teamId, Long userId, Long teamUserId) {
        redisLongTemplate.opsForSet().add("teamMemberUserId" + teamId, userId);
        redisLongTemplate.opsForSet().add("teamMember" + teamId + "userId:" + userId, teamUserId);
        redisLongTemplate.opsForSet().add("teamMember" + teamId + "teamUserId:" + teamUserId, userId);
    }

    public void deleteMember(Long teamId, Long userId, Long teamUserId) {
        redisLongTemplate.opsForSet().remove("teamMemberUserId" + teamId, userId);
        redisLongTemplate.opsForSet().remove("teamMember" + teamId + "userId:" + userId, teamUserId);
        redisLongTemplate.opsForSet().remove("teamMember" + teamId + "teamUserId:" + teamUserId, userId);
    }

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        return Boolean.TRUE.equals(
                redisLongTemplate.opsForSet().isMember("teamMember" + teamId + "userId:" + userId, teamUserId)
        );
    }

    public Set<Long> getTeamMembers(long teamId) {
        return redisLongTemplate.opsForSet().members("teamMemberUserId" + teamId);
    }

    public void updateUserChatroom(long userId, long teamId, Double timestamp) {
        redisLongTemplate.opsForZSet().add("userChatroom" + userId, teamId, timestamp);
    }

    public void deleteUserChatroom(long userId, long teamId) {
        redisLongTemplate.opsForZSet().remove("userChatroom" + userId, teamId);
    }

    public List<Long> getUserChatroom(long userId) {
        Set<ZSetOperations.TypedTuple<Long>> result
                = redisLongTemplate.opsForZSet()
                .reverseRangeWithScores("userChatroom" + userId, 0, 19);

        return (result == null) ? new ArrayList<>() :
                result.stream()
                        .map(ZSetOperations.TypedTuple::getValue)
                        .collect(Collectors.toList());
    }

    public void saveMessage(long teamId, RedisChatMessage message) {
        Long messageId = redisChatMessageTemplate.opsForValue().increment(messageIdKey);
        message.setId(messageId);

        redisChatMessageTemplate.opsForZSet().add("teamMessage" + teamId, message, message.getId());
    }

    public List<RedisChatMessage> getMessages(Long teamId, Long messageId) {
        String key = "teamMessage" + teamId;

        Set<ZSetOperations.TypedTuple<RedisChatMessage>> result;

        if (messageId < 0) {
            // maxId가 0보다 작으면 모든 요소에서 내림차순으로 100개를 가져오기
            result = redisChatMessageTemplate.opsForZSet()
                    .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 30);
            return getRedisChatMessages(result);
        }

        // maxId가 0 이상이면, maxId보다 작은 값을 내림차순으로 100개 가져옴
        result = redisChatMessageTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, Double.NEGATIVE_INFINITY, messageId - 1, 0, 30);

        return getRedisChatMessages(result);
    }

    private static List<RedisChatMessage> getRedisChatMessages(
            Set<ZSetOperations.TypedTuple<RedisChatMessage>> result
    ) {
        if (result == null) {
            return Collections.emptyList();
        }

        return result.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .collect(Collectors.toList());
    }
}
