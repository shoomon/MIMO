package com.bisang.backend.chat.repository.chatroomuser;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
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

    public void insertRedisMemberUser(Long teamId, Long userId) {
        chatroomUserRedisRepository.insertMember(teamId, userId);
    }

    public void insertJpaMemberUser(ChatroomUser chatroomUser) {
        chatroomUserJpaRepository.save(chatroomUser);
    }

    @Transactional
    @Modifying
    public void removeMember(Long teamId, Long userId) {
        chatroomUserRedisRepository.deleteMember(teamId, userId);
        chatroomUserJpaRepository.deleteByChatroomIdAndUserId(teamId, userId);
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<Long> teamMembers = chatroomUserRedisRepository.getTeamMembers(teamId);
        if (teamMembers.isEmpty()) {
            teamMembers = chatroomUserJpaRepository.findUserIdsByTeamId(teamId);
        }
        return teamMembers;
    }

    public boolean isMember(Long teamId, Long userId) {
        if (chatroomUserRedisRepository.isMember(teamId, userId)) {
            return true;
        }

        if (chatroomUserJpaRepository.existsByUserIdAndChatroomId(
                userId,
                teamId
        )) {
            chatroomUserRedisRepository.insertMember(teamId, userId);
            return true;
        }

        return false;
    }

    public Map<Object, Object> getUserInfo(Long chatroomId, Long userId) {
        Map<Object, Object> userInfo = redisCacheRepository.getUserProfile(chatroomId, userId);

        if (userInfo.isEmpty()) {
            String nickname = chatroomUserJpaRepository.findNicknameByTeamIdAndUserId(chatroomId, userId);

            User user = userJpaRepository.findById(userId).orElseThrow(() -> new AccountException(NOT_FOUND));
            String profileImage = user.getProfileUri();

            redisCacheRepository.cacheUserProfile(chatroomId, userId, nickname, profileImage);

            userInfo.put("nickname", nickname);
            userInfo.put("profileImage", profileImage);
        }

        return userInfo;
    }

    @Transactional
    public void updateNickname(Long userId, Long teamId, String nickname) {

        redisCacheRepository.updateUserNickName(teamId, userId, nickname);

        ChatroomUser user = chatroomUserJpaRepository
                .findByChatroomIdAndUserId(teamId, userId)
                .orElseThrow(() -> new AccountException(NOT_FOUND));
        user.setNickname(nickname);
    }

    @Transactional
    public void updateLastRead(Long userId, LocalDateTime lastDateTime, Long roomId, Long lastChatId) {
        //TODO: db에 어떻게 저장할지 생각해봐야함. 저장 해야하나..? 어차피 실시간이 아닌데?
        chatroomUserRedisRepository.insertLastReadScore(roomId, userId, lastDateTime, lastChatId);
    }

    public double getLastReadScore(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadScore(chatroomId, userId);
    }

    public Long getLastReadChatId(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadChatId(chatroomId, userId);
    }
}
