package com.bisang.backend.chat.repository.chatroomRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatroomRedisRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;

    //유저별 채팅방 찾을 때 사용할 키
    private static final String chatroomKey = "userChatroom:";

    public void updateUserChatroom(long userId, long teamId, Double timestamp) {
        redisLongTemplate.opsForZSet().add(chatroomKey + userId, teamId, timestamp);
    }

    public void deleteUserChatroom(long userId, long teamId) {
        redisLongTemplate.opsForZSet().remove(chatroomKey + userId, teamId);
    }

    public List<Long> getUserChatroom(long userId) {
        Set<ZSetOperations.TypedTuple<Long>> result
                = redisLongTemplate.opsForZSet()
                .reverseRangeWithScores(chatroomKey + userId, 0, 19);

        return (result == null) ? new ArrayList<>() :
                result.stream()
                        .map(ZSetOperations.TypedTuple::getValue)
                        .collect(Collectors.toList());
    }

}
