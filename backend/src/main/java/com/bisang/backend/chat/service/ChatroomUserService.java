package com.bisang.backend.chat.service;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.chatroom.ChatroomJpaRepository;
import com.bisang.backend.chat.repository.chatroom.ChatroomRepository;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.bisang.backend.common.exception.ChatroomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final ChatMessageService chatMessageService;
    private final ChatroomRepository chatroomRepository;
    private final ChatRedisService chatRedisService;

    public boolean isMember(Long chatroomId, Long userId) {
        return chatroomUserRepository.isMember(chatroomId, userId);
    }

    public void updateNickname(Long userId, Long teamId, String nickname) {
        Long chatroomId = chatroomJpaRepository
                .findIdByTeamId(teamId)
                .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));
        chatroomUserRepository.updateNickname(userId, chatroomId, nickname);
    }

    public void updateLastRead(Long id, Long lastDateTime, Long lastChatId, Long roomId) {
        chatroomUserRepository.updateLastRead(id, lastDateTime, roomId, lastChatId);
    }

    public void enterChatroom(Long teamId, Long userId, String nickname) {
        Long chatroomId = chatroomJpaRepository
                .findIdByTeamId(teamId)
                .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));

        Long enterDate = Instant.now().toEpochMilli();
        LocalDateTime localEnterDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(enterDate), ZoneId.of("UTC"));
        chatroomUserRepository.insertRedisMemberUser(chatroomId, userId);

        RedisChatMessage message = new RedisChatMessage(
                chatroomId,
                userId,
                nickname + "님이 입장하셨습니다.",
                enterDate,
                ChatType.ENTER
        );
        chatRedisService.afterSendMessage(chatroomId, message);

        ChatroomUser chatroomUser
                = ChatroomUser.createChatroomUser(
                        chatroomId,
                        userId,
                        nickname,
                        localEnterDate,
                        message.getId()
                );

        chatroomUserRepository.insertJpaMemberUser(chatroomUser);
        chatroomUserRepository.insertTeamEnterScore(enterDate, message.getId(), chatroomId, userId);
        chatroomUserRepository.updateLastRead(userId, message.getTimestamp(), chatroomId, message.getId());

        chatMessageService.broadcastMessage(chatroomId, message);
    }

    public boolean leaveChatroom(Long userId, Long teamId) {
        Long chatroomId = chatroomJpaRepository
                .findIdByTeamId(teamId)
                .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));

        Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(chatroomId, userId);
        String nickname = (String)userInfo.get("nickname");

        Long currentTime = Instant.now().toEpochMilli();

        RedisChatMessage message = new RedisChatMessage(
                chatroomId,
                userId,
                nickname + "님이 퇴장하였습니다.",
                currentTime,
                ChatType.LEAVE
        );

        removeMember(userId, chatroomId);

        chatRedisService.afterSendMessage(chatroomId, message);
        chatMessageService.broadcastMessage(chatroomId, message);

        return true;
    }

    public void forceLeave(Long teamId, Long userId) {
        Long chatroomId = chatroomJpaRepository
                .findIdByTeamId(teamId)
                .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));

        Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(chatroomId, userId);
        String nickname = (String)userInfo.get("nickname");

        Long currentTime = Instant.now().toEpochMilli();

        RedisChatMessage redisChatMessage = new RedisChatMessage(
                chatroomId,
                userId,
                nickname + "님이 강제퇴장 되었습니다.",
                currentTime,
                ChatType.FORCE
        );

        chatRedisService.afterSendMessage(chatroomId, redisChatMessage);
        chatMessageService.broadcastMessage(chatroomId, redisChatMessage);
        removeMember(userId, chatroomId);
    }

    private void removeMember(Long userId, Long chatroomId) {
        chatroomUserRepository.removeMember(chatroomId, userId);
        chatroomRepository.redisDeleteUserChatroom(userId, chatroomId);
    }
}
