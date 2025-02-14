package com.bisang.backend.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.repository.chatroom.ChatroomRepository;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.bisang.backend.chat.repository.message.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserService chatroomUserService;

    public void createChatroom(
            Long userId,
            Long teamId,
            String nickname,
            String title,
            String profileUri,
            ChatroomStatus status
    ) {
        Chatroom chatroom = Chatroom.createTeamChatroom(userId, teamId, title, profileUri, status);

        chatroomRepository.insertChatroom(chatroom);

        chatroomUserService.enterChatroom(chatroom.getId(), userId, nickname);
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

            double lastReadScore = chatroomUserRepository.getLastReadScore(chatroomId, userId);
            Long lastReadChatId = chatroomUserRepository.getLastReadChatId(chatroomId, userId);
            Long unreadCount = chatMessageRepository.calculateUnreadCount(chatroomId, lastReadScore, lastReadChatId);

            ChatroomResponse cr = new ChatroomResponse(chatroomId,
                    (String)chatroomInfo.get("title"),
                    (String)chatroomInfo.get("profileUri"),
                    (String)lastChat.get("lastChat"),
                    (LocalDateTime)lastChat.get("lastDatetime"),
                    (String)userInfo.get("nickname"),
                    unreadCount
            );

            chatroomResponse.add(cr);
        }

        return chatroomResponse;
    }

    //TODO: 팀 프로필 업데이트 되면 호출해줘야함
    public void updateChatroomProfileUri(Long teamId, String profileUri) {
        Long chatroomId = chatroomRepository.getChatroomIdByteamId(teamId);
        chatroomRepository.updateChatroomProfileUri(chatroomId, profileUri);
    }
}