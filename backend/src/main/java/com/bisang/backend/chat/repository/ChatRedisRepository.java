package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final RedisTemplate<String, Object> template;
    private final RedisTemplate<String, RedisTeamMember> redisTeamMemberTemplate;
    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;

    private static final String messageIdKey = "chat:message:id"; // 채팅 메시지 ID를 관리할 key

    public void insertMember(long teamId, RedisTeamMember teamMember) {
        redisTeamMemberTemplate.opsForSet().add("teamMember"+teamId, teamMember);
    }

    public void deleteMember(long teamId, RedisTeamMember teamMember) {
        redisTeamMemberTemplate.opsForSet().remove("teamMember"+teamId, teamMember);
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
        template.opsForZSet().add("userChatroom"+userId, teamId, timestamp);
    }

    public void deleteUserChatroom(long teamUserId, long teamId) {
        template.opsForZSet().remove("userChatroom"+teamUserId, teamId);
    }

    public void saveMessage(long teamId, RedisChatMessage message) {
        Long messageId = redisChatMessageTemplate.opsForValue().increment(messageIdKey);
        message.setId(messageId);

        redisChatMessageTemplate.opsForList().rightPush("teamMessage"+teamId, message);
    }

    public List<RedisChatMessage> getMessages(Long teamId) {
        String key = "teamMessage"+teamId;

        Long size = redisChatMessageTemplate.opsForList().size(key);
        if (size == null || size < 100) {
            return redisChatMessageTemplate.opsForList().range(key, 0, size == null ? -1 : size - 1);
        }

        return redisChatMessageTemplate.opsForList().range(key, 0, 99);
    }
}
