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

    public void cacheUserProfile(Long chatroomId, Long userId, String name, String imageUrl) {
        String key = USER_KEY + chatroomId + ":" + userId;
        redisTemplate.opsForHash().put(key, "nickname", name);
        redisTemplate.opsForHash().put(key, "profileImage", imageUrl);
    }

    public Map<Object, Object> getUserProfile(Long chatroomId, Long userId) {
        String key = USER_KEY + chatroomId + ":" + userId;
        return redisTemplate.opsForHash().entries(key);
    }

    public void updateUserNickName(Long chatroomId, Long userId, String nickname) {
        redisTemplate.opsForHash().put(USER_KEY + chatroomId + ":" + userId, "nickname", nickname);
    }

    public void updateUserProfileUri(Long chatroomId, Long userId, String profileUri) {
        redisTemplate.opsForHash().put(USER_KEY + chatroomId + ":" + userId, "profileImage", profileUri);

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

    public void updateChatroomProfileUri(Long teamId, String profileUri) {
        String key = CHATROOM_KEY + teamId;
        redisTemplate.opsForHash().put(key, "profileUri", profileUri);
    }
}
