package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.chatroom.ChatroomJpaRepository;
import com.bisang.backend.chat.repository.chatroom.ChatroomRepository;
import org.springframework.stereotype.Service;

import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final ChatMessageService chatMessageService;
    private final ChatroomRepository chatroomRepository;

    public boolean isMember(Long teamId, Long userId) {
        return chatroomUserRepository.isMember(teamId, userId);
    }

    //TODO: 팀쪽에서 변경되면 호출해줘야함
    public void updateNickname(Long userId, Long teamId, String nickname) {
        Long chatroomId = chatroomJpaRepository.findIdByTeamId(teamId);
        chatroomUserRepository.updateNickname(userId, chatroomId, nickname);
    }

    public void updateLastRead(Long id, LocalDateTime lastDateTime, Long lastChatId, Long roomId) {
        chatroomUserRepository.updateLastRead(id, lastDateTime, roomId, lastChatId);
    }


    public void enterChatroom(Long chatroomId, Long userId, String nickname) {
        ChatroomUser chatroomUser = ChatroomUser.createChatroomUser(chatroomId, userId, nickname, LocalDateTime.now());

        chatroomUserRepository.insertJpaMemberUser(chatroomUser);

        RedisChatMessage message = new RedisChatMessage(
                chatroomId,
                userId,
                nickname + "님이 입장하셨습니다.",
                LocalDateTime.now(),
                ChatType.ENTER
        );

        chatroomUserRepository.insertRedisMemberUser(chatroomId, userId);
        chatroomUserRepository.updateLastRead(userId, message.getTimestamp(), chatroomId, userId);
        chatMessageService.broadcastMessage(chatroomId, message);
    }

    public boolean leaveChatroom(Long userId, Long teamId) {
        Long chatroomId = chatroomJpaRepository.findIdByTeamId(teamId);

        RedisChatMessage message = new RedisChatMessage(
                chatroomId,
                userId,
                "누군가 퇴장하였습니다.",
                LocalDateTime.now(),
                ChatType.LEAVE
        );

        removeMember(userId, chatroomId);
        chatMessageService.broadcastMessage(chatroomId, message);

        return true;
    }

    public void forceLeave(Long teamId, Long userId) {
        Long chatroomId = chatroomJpaRepository.findIdByTeamId(teamId);

        RedisChatMessage redisChatMessage = new RedisChatMessage(
                chatroomId,
                userId,
                "누군가 강제퇴장 되었습니다.",
                LocalDateTime.now(),
                ChatType.FORCE
        );

        chatMessageService.broadcastMessage(chatroomId, redisChatMessage);
        removeMember(userId, chatroomId);
    }

    private void removeMember(Long userId, Long chatroomId) {
        chatroomUserRepository.removeMember(chatroomId, userId);
        chatroomRepository.redisDeleteUserChatroom(userId, chatroomId);
    }
}
