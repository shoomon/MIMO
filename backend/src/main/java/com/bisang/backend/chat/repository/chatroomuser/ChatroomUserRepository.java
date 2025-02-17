package com.bisang.backend.chat.repository.chatroomuser;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.hibernate.NonUniqueResultException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatroomUserRepository {

    private final ChatroomUserRedisRepository chatroomUserRedisRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

    public void insertRedisMemberUser(Long chatroomId, Long userId) {
        chatroomUserRedisRepository.insertMember(chatroomId, userId);
    }

    public void insertJpaMemberUser(ChatroomUser chatroomUser) {
        chatroomUserJpaRepository.save(chatroomUser);
    }

    @Transactional
    @Modifying
    public void removeMember(Long chatroomId, Long userId) {
        chatroomUserRedisRepository.deleteMember(chatroomId, userId);
        chatroomUserJpaRepository.deleteByChatroomIdAndUserId(chatroomId, userId);
    }

    public Set<Long> getTeamMembers(long chatroomId) {
        Set<Long> teamMembers = chatroomUserRedisRepository.getTeamMembers(chatroomId);
        if (teamMembers.isEmpty()) {
            teamMembers = chatroomUserJpaRepository.findUserIdsByTeamId(chatroomId);
        }
        return teamMembers;
    }

    public boolean isMember(Long chatroomId, Long userId) {
        if (chatroomUserRedisRepository.isMember(chatroomId, userId)) {
            return true;
        }

        if (chatroomUserJpaRepository.existsByUserIdAndChatroomId(
                userId,
                chatroomId
        )) {
            chatroomUserRedisRepository.insertMember(chatroomId, userId);
            return true;
        }

        return false;
    }

    public Map<Object, Object> getUserInfo(Long chatroomId, Long userId) {
        Map<Object, Object> userInfo = redisCacheRepository.getUserProfile(chatroomId, userId);

        String nickname;
        if (userInfo.isEmpty()) {
            try {
                nickname = chatroomUserJpaRepository.findNicknameByChatroomIdAndUserId(chatroomId, userId);
            } catch (NonUniqueResultException e) {
                log.error("채팅룸 중복 입장");
                nickname = "중복처리 필요합니다";
            }

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

    public void updateLastRead(Long userId, LocalDateTime lastDateTime, Long roomId, Long lastChatId) {
        chatroomUserRedisRepository.insertLastReadScore(roomId, userId, lastDateTime, lastChatId);
    }

    public Double getLastReadScore(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadScore(chatroomId, userId);
    }

    public Long getLastReadChatId(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadChatId(chatroomId, userId);
    }
}
