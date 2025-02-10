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
import com.bisang.backend.chat.repository.chatroom.ChatroomRepository;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.bisang.backend.chat.repository.message.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomRepository chatroomRepository;

    private final SimpMessagingTemplate template;

    public void broadcastMessage(Long teamId, RedisChatMessage message) {
        Set<Long> teamMembers = chatroomUserRepository.getTeamMembers(teamId);

        //여기에 teamMember 없으면 log 찍어야하나? 없을 수 없는 곳인데 없는 경우임

        for (Long userId : teamMembers) {
            chatroomRepository.redisUpdateUserChatroom(
                    userId,
                    teamId,
                    (double)message.getTimestamp().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
        }
        chatMessageRepository.redisSaveMessage(teamId, message);

        Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(message.getTeamUserId(), message.getUserId());
        ChatMessageResponse messageResponse = new ChatMessageResponse(
                message.getId(),
                message.getTeamUserId(),
                (String)userInfo.get("nickname"),
                (String)userInfo.get("profileImage"),
                message.getChat(),
                message.getTimestamp(),
                message.getType()
        );

        //template.convertAndSend(messageResponse);
    }

    public List<ChatMessageResponse> getMessages(Long roomId, Long messageId) {
        List<RedisChatMessage> messageList = chatMessageRepository.getMessages(roomId, messageId);
        List<ChatMessageResponse> responseList = new LinkedList<>();

        for (RedisChatMessage message : messageList) {
            Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(
                    message.getTeamUserId(),
                    message.getUserId()
            );
            ChatMessageResponse messageResponse = new ChatMessageResponse(
                    message.getId(),
                    message.getTeamUserId(),
                    (String)userInfo.get("nickname"),
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
