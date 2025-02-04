package com.bisang.backend.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final RedisTemplate<String, Object> template;

    public void insertMember(long teamId, long userId) {
        template.opsForSet().add("teamMember"+teamId, userId);
    }

    public void deleteMember(long teamId, long userId) {
        template.opsForSet().remove("teamMember"+teamId, userId);
    }
}
