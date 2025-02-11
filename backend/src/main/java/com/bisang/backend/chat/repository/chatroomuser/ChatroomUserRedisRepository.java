package com.bisang.backend.chat.repository.chatroomuser;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomUserRedisRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;

    // chatroomId로 해당 채팅방에 속해있는 모든 유저의 userId를 저장하고 가져오는 키
    private static final String teamMemberKey = "teamMember:";


    public void insertMember(Long teamId, Long userId) {
        redisLongTemplate.opsForSet().add(teamMemberKey + teamId, userId);
    }

    public void deleteMember(Long teamId, Long userId) {
        redisLongTemplate.opsForSet().remove(teamMemberKey + teamId, userId);
    }

    public boolean isMember(Long teamId, Long userId) {
        return Boolean.TRUE.equals(redisLongTemplate.opsForSet().isMember(teamMemberKey + teamId, userId));
    }

    public Set<Long> getTeamMembers(long teamId) {
        return redisLongTemplate.opsForSet().members(teamMemberKey + teamId);
    }
}
