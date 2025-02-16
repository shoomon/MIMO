package com.bisang.backend.chat.repository.chatroom;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomRedisRepository {

    private final StringRedisTemplate redisTemplate;

    //유저별 채팅방 찾을 때 사용할 키
    private static final String chatroomKey = "userChatroom:";

    public void deleteUserChatroom(long userId, long chatroomId) {
        redisTemplate.opsForZSet().remove(chatroomKey + userId, String.valueOf(chatroomId));
    }

    public List<Long> getUserChatroom(long userId) {
        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet()
                .reverseRangeWithScores(chatroomKey + userId, 0, -1);

        if (result == null) {
            return null;
        }

        return result.stream()
                .map(tuple -> Long.valueOf(tuple.getValue()))
                .toList();
    }

}
