package com.bisang.backend.chat.repository.chatroomuser;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.hibernate.NonUniqueResultException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ChatroomException;
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
            ChatroomUser user = chatroomUserJpaRepository
                    .findByChatroomIdAndUserId(chatroomId, userId)
                    .orElseThrow(() -> new ChatroomException(NOT_FOUND));

            Long enterDate = user.getEnterDate().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
            insertRedisMemberUser(chatroomId, userId);
            insertTeamEnterScore(enterDate, user.getEnterChatId(), chatroomId, userId);

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

    public void updateLastRead(Long userId, Long lastDateTime, Long roomId, Long lastChatId) {
        chatroomUserRedisRepository.insertLastReadScore(roomId, userId, lastDateTime, lastChatId);
    }

    public Double getLastReadScore(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadScore(chatroomId, userId);
    }

    public Long getLastReadChatId(Long chatroomId, Long userId) {
        return chatroomUserRedisRepository.getLastReadChatId(chatroomId, userId);
    }

    public void insertTeamEnterScore(Long enterDate, Long enterChatId, Long chatroomId, Long userId) {
        double score = enterDate + (enterChatId % 1000) / 1000.0;
        chatroomUserRedisRepository.insertTeamEnterScore(chatroomId, userId, score, enterChatId);
    }

    public Double getTeamEnterScore(Long roomId, Long userId) {
        Double teamEnterScore = chatroomUserRedisRepository.getTeamEnterScore(roomId, userId);

        if (teamEnterScore == null) {
            ChatroomUser user = chatroomUserJpaRepository
                    .findByChatroomIdAndUserId(roomId, userId)
                    .orElseThrow(() -> new ChatroomException(NOT_FOUND));

            LocalDateTime enterDate = user.getEnterDate();
            teamEnterScore = enterDate
                    .toInstant(ZoneOffset.ofTotalSeconds(0))
                    .toEpochMilli() + (user.getEnterChatId() % 1000) / 1000.0;

            chatroomUserRedisRepository.insertTeamEnterScore(roomId, userId, teamEnterScore, user.getEnterChatId());
        }

        return teamEnterScore;
    }

    public Long getTeamEnterChatId(Long roomId, Long userId) {
        Long teamEnterChatId = chatroomUserRedisRepository.getTeamEnterChatId(roomId, userId);

        if (teamEnterChatId == null) {
            ChatroomUser user = chatroomUserJpaRepository
                    .findByChatroomIdAndUserId(roomId, userId)
                    .orElseThrow(() -> new ChatroomException(NOT_FOUND));

            chatroomUserRedisRepository.insertTeamEnterChatId(roomId, userId, user.getEnterChatId());
        }

        return teamEnterChatId;
    }
}
