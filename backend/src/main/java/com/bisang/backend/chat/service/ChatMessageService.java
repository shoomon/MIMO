package com.bisang.backend.chat.service;

import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRepository repository;
    private final SimpMessagingTemplate template;

    public void broadcastMessage(Long teamId, RedisChatMessage message) {
        Set<Long> teamMembers = repository.getTeamMembers(teamId);

        //여기에 teamMember 없으면 log 찍어야하나? 없을 수 없는 곳인데 없는 경우임

        for (Long userId : teamMembers) {
            repository.redisUpdateUserChatroom(
                    userId,
                    teamId,
                    (double)message.getTimestamp().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
        }
        repository.redisSaveMessage(teamId, message);

        Map<Object, Object> userInfo = repository.getUserInfo(message.getTeamUserId(), message.getUserId());
        ChatMessageResponse messageResponse = new ChatMessageResponse(
                message.getId(),
                message.getTeamUserId(),
                (String)userInfo.get("name"),
                (String)userInfo.get("profileImage"),
                message.getChat(),
                message.getTimestamp(),
                message.getType()
        );

        //template.convertAndSend(messageResponse);
    }

    public List<ChatMessageResponse> getMessages(Long roomId, Long messageId) {
        List<RedisChatMessage> messageList = repository.getMessages(roomId, messageId);
        List<ChatMessageResponse> responseList = new LinkedList<>();

        for (RedisChatMessage message : messageList) {
            Map<Object, Object> userInfo = repository.getUserInfo(message.getTeamUserId(), message.getUserId());
            ChatMessageResponse messageResponse = new ChatMessageResponse(
                    message.getId(),
                    message.getTeamUserId(),
                    (String)userInfo.get("name"),
                    (String)userInfo.get("profileImage"),
                    message.getChat(),
                    message.getTimestamp(),
                    message.getType()
            );
            responseList.add(messageResponse);
        }

        return responseList;
    }
}
