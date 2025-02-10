package com.bisang.backend.chat.repository;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.ChatroomStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_KEY = "user:";
    private static final String CHATROOM_KEY = "chatroom:";

    public void cacheUserProfile(Long teamUserId, String name, String imageUrl) {
        String key = USER_KEY + teamUserId;
        redisTemplate.opsForHash().put(key, "nickname", name);
        redisTemplate.opsForHash().put(key, "profileImage", imageUrl);
    }

    public Map<Object, Object> getUserProfile(Long teamUserId) {
        String key = USER_KEY + teamUserId;
        return redisTemplate.opsForHash().entries(key);
    }

    public void cacheChatroomInfo(Long chatroomId, String roomName, String profileUri, ChatroomStatus chatroomStatus) {
        String key = CHATROOM_KEY + chatroomId;
        redisTemplate.opsForHash().put(key, "title", roomName);
        redisTemplate.opsForHash().put(key, "profileUri", profileUri);
        redisTemplate.opsForHash().put(key, "chatroomStatus", chatroomStatus);
    }

    public Map<Object, Object> getChatroomInfo(Long chatroomId) {
        String key = CHATROOM_KEY + chatroomId;
        return redisTemplate.opsForHash().entries(key);
    }
}
