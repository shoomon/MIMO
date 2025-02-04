package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisUserChatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final ChatRedisRepository chatRedisRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;

    public void redisInsertMemberUser(long teamId, long teamUserId) {
        chatRedisRepository.insertMember(teamId, teamUserId);
    }

    public void redisRemoveMember(long teamId, long teamUserId) {
        chatRedisRepository.deleteMember(teamId, teamUserId);
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<Long> teamMembers = chatRedisRepository.getTeamMembers(teamId);
        if (teamMembers.isEmpty()) {
            teamMembers = chatroomUserJpaRepository.findUserIdsByTeamId(teamId);
        }
        return teamMembers;
    }

    public void redisUpdateUserChatroom(long teamUserId, RedisUserChatroom userChatroom, Double timestamp) {
        chatRedisRepository.updateUserChatroom(teamUserId, userChatroom, timestamp);
    }

    public void redisDeleteUserChatroom(long teamUserId, RedisUserChatroom userChatroom) {
        chatRedisRepository.deleteUserChatroom(teamUserId, userChatroom);
    }

    public void redisSaveMessage(long teamId, RedisChatMessage message) {
        chatRedisRepository.saveMessage(teamId, message);
    }

    public boolean isMember(Long teamUserId, Long teamId) {
        if (chatRedisRepository.isMember(teamUserId, teamId)) {
            return true;
        }
        return chatroomUserJpaRepository.existsByIdAndChatroomId(teamUserId, teamId);
    }

    public List<Object> redisGetMessages(Long teamId) {
        return chatRedisRepository.getMessages(teamId);
    }
}
