package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisUserChatroom;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository repository;
    private final SimpMessagingTemplate template;

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
        Set<Long> teamMembers = repository.getTeamMembers(teamId);

        for (Object userId : teamMembers) {
            RedisUserChatroom userChatroom = new RedisUserChatroom(teamId, teamName);
            repository.redisUpdateUserChatroom(Long.parseLong(userId.toString()), userChatroom, (double)message.getTimestamp().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
        }

        repository.redisSaveMessage(teamId, message);
        template.convertAndSend(message);
    }

    public boolean isMember(Long userId, Long teamId) {
        return repository.isMember(userId, teamId);
    }
}
