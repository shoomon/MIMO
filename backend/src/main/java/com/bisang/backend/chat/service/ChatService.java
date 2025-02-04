package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.response.ChatMessageResponse;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
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
     * @param teamId
     */
    public void enterChatroom(Long userId, Long teamId) {
        RedisChatMessage message = new RedisChatMessage(userId, "", LocalDateTime.now(), ChatType.ENTER);

        repository.redisInsertMemberUser(teamId, userId);
        broadcastMessage(teamId, message);
    }

    /**
     * 모임 탈퇴, 강퇴 시 호출
     * @param userId
     * @param teamId
     */
    public void leaveChatroom(Long userId, Long teamId) {
        RedisChatMessage message = new RedisChatMessage(userId, "", LocalDateTime.now(), ChatType.LEAVE);

        repository.redisRemoveMember(teamId, userId);
        repository.redisDeleteUserChatroom(teamId, teamId);
        broadcastMessage(teamId, message);
    }

    public void broadcastMessage(Long teamId, RedisChatMessage message) {
        Set<Long> teamMembers = repository.getTeamMembers(teamId);

        //여기에 teamMember 없으면 log 찍어야하나? 없을 수 없는 곳인데 없는 경우임

        for (Object userId : teamMembers) {
            repository.redisUpdateUserChatroom(
                    Long.parseLong(userId.toString()),
                    teamId,
                    (double)message.getTimestamp().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
        }
        repository.redisSaveMessage(teamId, message);

        Map<Object, Object> userInfo = repository.getUserInfo(message.getTeamUserId(), message.getUserId());
        ChatMessageResponse messageResponse = new ChatMessageResponse(
                message.getTeamUserId(),
                (String)userInfo.get("name"),
                (String)userInfo.get("profileImage"),
                message.getChat(),
                message.getTimestamp(),
                message.getType()
        );

        template.convertAndSend(messageResponse);
    }

    public boolean isMember(Long userId, Long teamId) {
        return repository.isMember(userId, teamId);
    }
}
