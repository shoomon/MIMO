package com.bisang.backend.chat.repository.chatroomuser;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import java.util.Map;
import java.util.Set;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class ChatroomUserRepository {

    private final ChatroomUserRedisRepository chatroomUserRedisRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

    public void insertRedisMemberUser(Long teamId, Long userId, Long teamUserId) {
        chatroomUserRedisRepository.insertMember(teamId, userId, teamUserId);
    }

    public void insertJpaMemberUser(ChatroomUser chatroomUser) {
        chatroomUserJpaRepository.save(chatroomUser);
    }

    public void removeMember(Long teamId, Long userId, Long teamUserId) {
        chatroomUserRedisRepository.deleteMember(teamId, userId, teamUserId);
        chatroomUserJpaRepository.deleteById(teamUserId);
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<Long> teamMembers = chatroomUserRedisRepository.getTeamMembers(teamId);
        if (teamMembers.isEmpty()) {
            teamMembers = chatroomUserJpaRepository.findUserIdsByTeamId(teamId);
        }
        return teamMembers;
    }

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        if (chatroomUserRedisRepository.isMember(teamId, userId, teamUserId)) {
            return true;
        }

        if (chatroomUserJpaRepository.existsByIdAndUserIdAndChatroomId(
                teamUserId,
                userId,
                teamId
        )) {
            chatroomUserRedisRepository.insertMember(teamId, userId, teamUserId);
            return true;
        }

        return false;
    }

    public Map<Object, Object> getUserInfo(Long teamUserId, Long userId) {
        Map<Object, Object> userInfo = redisCacheRepository.getUserProfile(teamUserId);

        if (userInfo.isEmpty()) {
            String nickname = chatroomUserJpaRepository.findNicknameById(teamUserId);

            User user = userJpaRepository.findById(userId).orElseThrow(() -> new AccountException(NOT_FOUND));
            String profileImage = user.getProfileUri();

            redisCacheRepository.cacheUserProfile(teamUserId, nickname, profileImage);

            userInfo.put("nickname", nickname);
            userInfo.put("profileImage", profileImage);
        }

        return userInfo;
    }

    public Long getTeamUserId(Long userId, Long teamId) {
        Long teamUserId = chatroomUserRedisRepository.getTeamUserId(userId, teamId);
        if (teamUserId == null) {
            return chatroomUserJpaRepository.findTeamUserIdByUserIdAndChatroomId(userId, teamId);
        }

        return teamUserId;
    }

    @Transactional
    public void updateNickname(Long userId, Long teamId, String nickname) {
        Long teamUserId = getTeamUserId(userId, teamId);
        redisCacheRepository.updateUserNickName(teamUserId, nickname);

        ChatroomUser user = chatroomUserJpaRepository.findById(teamUserId).orElseThrow(() -> new AccountException(NOT_FOUND));
        user.setNickname(nickname);
    }

    public void updateProfileUri(Long userId, Long teamId, String profileUri) {
        Long teamUserId = getTeamUserId(userId, teamId);
        redisCacheRepository.updateUserProfileUri(teamUserId, profileUri);
    }
}
