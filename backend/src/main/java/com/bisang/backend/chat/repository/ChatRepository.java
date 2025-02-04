package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final ChatRedisRepository chatRedisRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

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

    public void redisUpdateUserChatroom(long teamUserId, long teamId, Double timestamp) {
        chatRedisRepository.updateUserChatroom(teamUserId, teamId, timestamp);
    }

    public void redisDeleteUserChatroom(long teamUserId, long teamId) {
        chatRedisRepository.deleteUserChatroom(teamUserId, teamId);
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

    public Map<Object, Object> getUserInfo(Long teamUserId, Long userId) {
        Map<Object, Object> userInfo = redisCacheRepository.getUserProfile(teamUserId);
        if (userInfo.isEmpty()) {
            String nickname = chatroomUserJpaRepository.findNicknameById(teamUserId);
            //TODO: 유저쪽에서 프로필 이미지 받아오는 메서드 받아와서 넣어야함
            String profileImage = "";

            redisCacheRepository.cacheUserProfile(teamUserId, nickname, profileImage);

            userInfo.put("name", nickname);
            userInfo.put("profileImage", profileImage);
        }

        return userInfo;
    }
}
