package com.bisang.backend.chat.repository.chatroomUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatroomUserRedisRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;

    // chatroomId와 userId로 teamUserId를 찾을 수 있도록 해주는 키
    private static final String chatroomByUserKey = "chatroom:byUser:";
    // chatroomId와 teamUserId로 userId를 찾을 수 있도록 해주는 키
    private static final String chatroomByTeamUserKey = "chatroom:byTeamUser:";
    // chatroomId로 해당 채팅방에 속해있는 모든 유저의 userId를 저장하고 가져오는 키
    private static final String teamMemberKey = "teamMember:";


    public void insertMember(Long teamId, Long userId, Long teamUserId) {
        String byUserKey = chatroomByUserKey + teamId;
        String byTeamUserKey = chatroomByTeamUserKey + teamId;

        redisLongTemplate.opsForSet().add(teamMemberKey + teamId, userId);

        redisLongTemplate.opsForHash().put(byUserKey, userId, teamUserId);
        redisLongTemplate.opsForHash().put(byTeamUserKey, teamUserId, userId);
    }

    public void deleteMember(Long teamId, Long userId, Long teamUserId) {
        redisLongTemplate.opsForSet().remove(teamMemberKey + teamId, userId);
        redisLongTemplate.opsForHash().delete(chatroomByUserKey + teamId + userId, teamUserId);
        redisLongTemplate.opsForHash().delete(chatroomByTeamUserKey + teamId + teamUserId, userId);
    }

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        String key = chatroomByUserKey + teamId;
        return redisLongTemplate.opsForHash().get(key, userId) == teamUserId;
    }

    public Set<Long> getTeamMembers(long teamId) {
        return redisLongTemplate.opsForSet().members(teamMemberKey + teamId);
    }

    public Long getTeamUserId(Long userId, Long teamId) {
        String key = chatroomByUserKey + teamId;
        return (Long)redisLongTemplate.opsForHash().get(key, userId);
    }
}
