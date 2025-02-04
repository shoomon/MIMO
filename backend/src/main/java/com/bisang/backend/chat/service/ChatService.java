package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    /**
     * 새로운 모임에 가입 시 호출
     * @param userId
     * @param nickname
     * @param teamId
     * @param teamName
     */
    public void enterChatroom(Long userId, String nickname, Long teamId, String teamName) {
        RedisChatMessage message = new RedisChatMessage(userId, nickname, "", LocalDateTime.now(), ChatType.ENTER);

        repository.redisInsertMemberUser(teamId, userId);
        broadcastMessage(teamId, message, teamName);
    }

    public void leaveChatroom(Long userId, String nickname, Long teamId, String teamName) {
        RedisChatMessage message = new RedisChatMessage(userId, nickname, "", LocalDateTime.now(), ChatType.LEAVE);

        repository.redisRemoveMember(teamId, userId);
        broadcastMessage(teamId, message, teamName);
    }

    public void broadcastMessage(Long teamId, RedisChatMessage message, String teamName) {

    }

}
