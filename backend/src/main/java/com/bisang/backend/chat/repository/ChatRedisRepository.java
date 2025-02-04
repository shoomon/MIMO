package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisUserChatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final RedisTemplate<String, Object> template;

    private static final String messageIdKey = "chat:message:id"; // 채팅 메시지 ID를 관리할 key

    public void insertMember(long teamId, long teamUserId) {
        template.opsForSet().add("teamMember"+teamId, teamUserId);
    }

    public void deleteMember(long teamId, long teamUserId) {
        template.opsForSet().remove("teamMember"+teamId, teamUserId);
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<Object> members = template.opsForSet().members("teamMember" + teamId);

        if (members == null) {
            return Collections.emptySet();
        }

        // Set<Long>으로 캐스팅
        return members.stream()
                .map(m -> (Long) m)
                .collect(Collectors.toSet());
    }

    public void updateUserChatroom(long teamUserId, RedisUserChatroom userChatroom, Double timestamp) {
        template.opsForZSet().add("userChatroom"+teamUserId, userChatroom, timestamp);
    }

    public void deleteUserChatroom(long teamUserId, RedisUserChatroom userChatroom) {
        template.opsForZSet().remove("userChatroom"+teamUserId, userChatroom);
    }

    public void saveMessage(long teamId, RedisChatMessage message) {
        Long messageId = template.opsForValue().increment(messageIdKey);
        message.setId(messageId);

        template.opsForList().rightPush("teamMessage"+teamId, message);
    }

    public boolean isMember(Long teamUserId, Long teamId) {
        return Boolean.TRUE.equals(template.opsForSet().isMember("teamMember" + teamId, teamUserId));
    }

    public List<Object> getMessages(Long teamId) {
        String key = "teamMessage"+teamId;

        Long size = template.opsForList().size(key);
        if (size == null || size < 100) {
            //TODO: DB에 더 저장된 게 있는지 확인해야함
            return template.opsForList().range(key, 0, size == null ? -1 : size - 1);
        }

        return template.opsForList().range(key, 0, 99);
    }
}
