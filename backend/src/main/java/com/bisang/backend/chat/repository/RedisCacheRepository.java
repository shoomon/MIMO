package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.ChatroomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_KEY = "user:";
    private static final String CHATROOM_KEY = "chatroom:";

    public void cacheUserProfile(Long userId, String name, String imageUrl) {
        String key = USER_KEY + userId;
        redisTemplate.opsForHash().put(key, "name", name);
        redisTemplate.opsForHash().put(key, "profileImage", imageUrl);
    }

    public Map<Object, Object> getUserProfile(Long teamUserId) {
        String key = USER_KEY + teamUserId;
        return redisTemplate.opsForHash().entries(key);
    }

    public void cacheChatroomInfo(Long chatroomId, String roomName, ChatroomStatus chatroomStatus) {
        String key = CHATROOM_KEY + chatroomId;
        redisTemplate.opsForHash().put(key, "roomName", roomName);
        redisTemplate.opsForHash().put(key, "chatroomStatus", chatroomStatus);
    }

    public Map<Object, Object> getChatroomInfo(Long chatroomId) {
        String key = CHATROOM_KEY + chatroomId;
        return redisTemplate.opsForHash().entries(key);
    }
}
