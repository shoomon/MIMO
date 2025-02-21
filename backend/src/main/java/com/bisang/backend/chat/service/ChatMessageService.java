package com.bisang.backend.chat.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.bisang.backend.chat.repository.message.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomUserRepository chatroomUserRepository;

    private final SimpMessagingTemplate template;

    public void broadcastMessage(Long chatroomId, RedisChatMessage message) {

        LocalDateTime localDateTime
                = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getTimestamp()), ZoneId.of("UTC"));

        Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(chatroomId, message.getUserId());
        ChatMessageResponse messageResponse = new ChatMessageResponse(
                message.getId(),
                message.getUserId(),
                (String)userInfo.get("nickname"),
                (String)userInfo.get("profileImage"),
                message.getChat(),
                localDateTime,
                message.getType()
        );

        template.convertAndSend("/sub/chat/" + chatroomId, messageResponse);
    }

    public List<ChatMessageResponse> getMessages(Long userId, Long roomId, Long messageId, String timestamp) {
        Double teamEnterScore = chatroomUserRepository.getTeamEnterScore(roomId, userId);
        Long teamEnterChatId = chatroomUserRepository.getTeamEnterChatId(roomId, userId);

        List<RedisChatMessage> messageList
                = chatMessageRepository.getMessages(roomId, messageId, timestamp, teamEnterScore, teamEnterChatId);
        List<ChatMessageResponse> responseList = new LinkedList<>();

        for (RedisChatMessage message : messageList) {
            Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(
                    roomId,
                    message.getUserId()
            );

            LocalDateTime localDateTime
                    = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getTimestamp()), ZoneId.of("UTC"));
            ChatMessageResponse messageResponse = new ChatMessageResponse(
                    message.getId(),
                    message.getUserId(),
                    (String)userInfo.get("nickname"),
                    (String)userInfo.get("profileImage"),
                    message.getChat(),
                    localDateTime,
                    message.getType()
            );
            responseList.add(messageResponse);
        }

        return responseList;
    }
}
