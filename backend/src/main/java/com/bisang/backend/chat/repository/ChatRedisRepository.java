package com.bisang.backend.chat.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final RedisTemplate<String, Object> template;
    private final RedisTemplate<String, RedisTeamMember> redisTeamMemberTemplate;
    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;

    private static final String messageIdKey = "chat:message:id"; // 채팅 메시지 ID를 관리할 key

    public void insertMember(long teamId, RedisTeamMember teamMember) {
        redisTeamMemberTemplate.opsForSet().add("teamMember" + teamId, teamMember);
    }

    public void deleteMember(long teamId, RedisTeamMember teamMember) {
        redisTeamMemberTemplate.opsForSet().remove("teamMember" + teamId, teamMember);
    }

    public boolean isMember(Long teamId, RedisTeamMember teamMember) {
        return Boolean.TRUE.equals(redisTeamMemberTemplate.opsForSet().isMember("teamMember" + teamId, teamMember));
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<RedisTeamMember> members = redisTeamMemberTemplate.opsForSet().members("teamMember" + teamId);

        if (members == null) {
            return Collections.emptySet();
        }

        Set<Long> memberId = new HashSet<>();
        for (RedisTeamMember m : members) {
            memberId.add(m.getUserId());
        }

        return memberId;
    }

    public void updateUserChatroom(long userId, long teamId, Double timestamp) {
        template.opsForZSet().add("userChatroom" + userId, teamId, timestamp);
    }

    public void deleteUserChatroom(long teamUserId, long teamId) {
        template.opsForZSet().remove("userChatroom" + teamUserId, teamId);
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
