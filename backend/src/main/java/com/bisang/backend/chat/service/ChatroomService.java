package com.bisang.backend.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
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
    private final ChatMessageService chatMessageService;


    public void createChatroom(Long userId, String nickname, String title, String profileUri, ChatroomStatus status) {
        Chatroom chatroom = Chatroom.createChatroom(userId, title, profileUri, status);

        chatroomRepository.insertChatroom(chatroom);

        enterChatroom(chatroom.getId(), userId, nickname);
    }

    public void enterChatroom(Long teamId, Long userId, String nickname) {
        ChatroomUser chatroomUser = ChatroomUser.createChatroomUser(teamId, userId, nickname, LocalDateTime.now());

        chatroomUserRepository.insertJpaMemberUser(chatroomUser);

        RedisChatMessage message = new RedisChatMessage(
                teamId,
                userId,
                nickname + "님이 입장하셨습니다.",
                LocalDateTime.now(),
                ChatType.ENTER
        );

        chatroomUserRepository.insertRedisMemberUser(teamId, userId);
        chatMessageService.broadcastMessage(teamId, message);
    }

    public boolean leaveChatroom(Long userId, Long teamId) {
        RedisChatMessage message = new RedisChatMessage(
                teamId,
                userId,
                "누군가 퇴장하였습니다.",
                LocalDateTime.now(),
                ChatType.LEAVE);

        chatroomUserRepository.removeMember(teamId, userId);
        chatroomRepository.redisDeleteUserChatroom(userId, teamId);
        chatMessageService.broadcastMessage(teamId, message);

        return true;
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

            ChatroomResponse cr = new ChatroomResponse(chatroomId,
                    (String)chatroomInfo.get("title"),
                    (String)chatroomInfo.get("profileUri"),
                    (String)lastChat.get("lastChat"),
                    (LocalDateTime)lastChat.get("lastDatetime"),
                    (String)userInfo.get("nickname")
            );

            chatroomResponse.add(cr);
        }

        return chatroomResponse;
    }

    //TODO: 팀 프로필 업데이트 되면 호출해줘야함
    public void updateChatroomProfileUri(Long teamId, String profileUri) {
        chatroomRepository.updateChatroomProfileUri(teamId, profileUri);
    }
}