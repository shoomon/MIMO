package com.bisang.backend.chat.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.repository.chatroom.ChatroomRepository;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.bisang.backend.chat.repository.message.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatroomService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserService chatroomUserService;

    @Transactional
    public void createChatroom(
            Long userId,
            Long teamId,
            String nickname,
            String title,
            String profileUri,
            ChatroomStatus status
    ) {
        Chatroom chatroom = Chatroom.createTeamChatroom(userId, teamId, title, profileUri, status);

        chatroomRepository.insertJpaChatroom(chatroom);
        chatroomUserService.enterChatroom(teamId, userId, nickname);
    }

    public List<ChatroomResponse> getChatroom(Long userId) {
        List<Long> chatroom = chatroomRepository.getUserChatroom(userId);

        if (chatroom.isEmpty()) {
            return null;
        }

        List<ChatroomResponse> chatroomResponse  = new ArrayList<>();
        for (Long chatroomId : chatroom) {
            Map<Object, Object> chatroomInfo = chatroomRepository.getChatroomInfo(chatroomId);
            Map<String, Object> lastChat = chatMessageRepository.getLastChat(chatroomId);
            Map<Object, Object> userInfo = chatroomUserRepository.getUserInfo(chatroomId, userId);

            Long unreadCount = 0L;
            try {
                Double lastReadScore = chatroomUserRepository.getLastReadScore(chatroomId, userId);
                Long lastReadChatId = chatroomUserRepository.getLastReadChatId(chatroomId, userId);
                unreadCount = chatMessageRepository.calculateUnreadCount(chatroomId, lastReadScore, lastReadChatId);
            } catch (NullPointerException e) {
                log.error("lastReadScore가 없습니다");
            }

            Long lastChatDatetime = (Long)lastChat.get("lastDatetime");
            LocalDateTime lastLocalDateTime
                    = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastChatDatetime), ZoneId.of("UTC"));

            ChatroomResponse cr = new ChatroomResponse(chatroomId,
                    (String)chatroomInfo.get("title"),
                    (String)chatroomInfo.get("profileUri"),
                    (String)lastChat.get("lastChat"),
                    lastLocalDateTime,
                    (String)userInfo.get("nickname"),
                    unreadCount
            );

            chatroomResponse.add(cr);
        }

        return chatroomResponse;
    }

    public void updateChatroomProfileUri(Long teamId, String profileUri) {
        Long chatroomId = chatroomRepository.getChatroomIdByteamId(teamId);
        chatroomRepository.updateChatroomProfileUri(chatroomId, profileUri);
    }

    public void updateChatroomTitle(Long teamId, String chatroomTitle) {
        Long chatroomId = chatroomRepository.getChatroomIdByteamId(teamId);
        chatroomRepository.updateChatroomTitle(chatroomId, chatroomTitle);
    }
}