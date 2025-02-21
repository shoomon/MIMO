package com.bisang.backend.chat.repository.chatroomuser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomUserRedisRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;
    private final RedisTemplate<String, Double> redisDoubleTemplate;

    // chatroomId로 해당 채팅방에 속해있는 모든 유저의 userId를 저장하고 가져오는 키
    private static final String teamMemberKey = "teamMember:";
    private static final String teamEnterScore = "teamEnterScore:";
    private static final String teamEnterChatId = "teamEnterChatId:";
    // chatroomId, userId로 유저가 가장 마지막으로 읽은 메시지 score와 id를 저장하고 가져오는 키
    private static final String lastReadScoreKey = "lastReadScore:";
    private static final String lastReadIdKey = "lastReadId:";

    public void insertMember(Long chatroomId, Long userId) {
        redisLongTemplate.opsForSet().add(teamMemberKey + chatroomId, userId);
    }

    public void insertTeamEnterScore(Long chatroomId, Long userId, Double score, Long enterChatId) {
        redisDoubleTemplate.opsForValue().set(teamEnterScore + chatroomId + ":" + userId, score);
        redisLongTemplate.opsForValue().set(teamEnterChatId + chatroomId + ":" + userId, enterChatId);
    }

    public void insertTeamEnterChatId(Long chatroomId, Long userId, Long enterChatId) {
        redisLongTemplate.opsForValue().set(teamEnterChatId + chatroomId + ":" + userId, enterChatId);
    }

    public void deleteMember(Long chatroomId, Long userId) {
        redisLongTemplate.opsForSet().remove(teamMemberKey + chatroomId, userId);
    }

    public Double getTeamEnterScore(Long chatroomId, Long userId) {
        return redisDoubleTemplate.opsForValue().get(teamEnterScore + chatroomId + ":" + userId);
    }

    public Long getTeamEnterChatId(Long chatroomId, Long userId) {
        return redisLongTemplate.opsForValue().get(teamEnterChatId + chatroomId + ":" + userId);
    }

    public boolean isMember(Long chatroomId, Long userId) {
        return Boolean.TRUE.equals(redisLongTemplate.opsForSet().isMember(teamMemberKey + chatroomId, userId));
    }

    public Set<Long> getTeamMembers(long chatroomId) {
        return redisLongTemplate.opsForSet().members(teamMemberKey + chatroomId);
    }

    public void insertLastReadScore(Long chatroomId, Long userId, Long lastDateTime, Long lastChatId) {
        double score = lastDateTime + (lastChatId % 1000) / 1000.0;
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
