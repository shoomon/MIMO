package com.bisang.backend.chat.repository.chatroomuser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomUserRedisRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;
    private final RedisTemplate<String, Double> redisDoubleTemplate;
    private final StringRedisTemplate redisTemplate;

    // chatroomId로 해당 채팅방에 속해있는 모든 유저의 userId를 저장하고 가져오는 키
    private static final String teamMemberKey = "teamMember:";
    // chatroomId, userId로 유저가 가장 마지막으로 읽은 메시지 score와 id를 저장하고 가져오는 키
    private static final String lastReadScoreKey = "lastReadScore:";
    private static final String lastReadIdKey = "lastReadId:";

    public void insertMember(Long teamId, Long userId) {
        redisLongTemplate.opsForSet().add(teamMemberKey + teamId, userId);
    }

    public void deleteMember(Long teamId, Long userId) {
        redisTemplate.opsForSet().remove(teamMemberKey + teamId, userId);
    }

    public boolean isMember(Long teamId, Long userId) {
        return Boolean.TRUE.equals(redisLongTemplate.opsForSet().isMember(teamMemberKey + teamId, userId));
    }

    public Set<Long> getTeamMembers(long teamId) {
        return redisLongTemplate.opsForSet().members(teamMemberKey + teamId);
    }

    public void insertLastReadScore(Long chatroomId, Long userId, LocalDateTime lastDateTime, Long lastChatId) {
        double score = lastDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() + (lastChatId % 1000) / 1000.0;
        System.out.println("calculate:" + score);
        redisDoubleTemplate.opsForValue().set(lastReadScoreKey + chatroomId + ":" + userId, score);
        redisLongTemplate.opsForValue().set(lastReadIdKey + chatroomId + ":" + userId, lastChatId);
    }

    public Double getLastReadScore(Long chatroomId, Long userId) {
        return redisDoubleTemplate.opsForValue().get(lastReadScoreKey + chatroomId + ":" + userId);
    }

    public Long getLastReadChatId(Long chatroomId, Long userId) {
        return redisLongTemplate.opsForValue().get(lastReadIdKey + chatroomId + ":" + userId);
    }
}
