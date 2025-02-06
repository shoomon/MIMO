package com.bisang.backend.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import com.bisang.backend.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRepository repository;
    private final ChatMessageService chatMessageService;


    public void createChatroom(Long userId, String nickname, String title, ChatroomStatus status) {
        Chatroom chatroom = Chatroom.createChatroom(userId, title, status);

        //TODO: 팀사진도 같이 넣으면 좋겠는데 그럼 db 바꿔야하는데 바꿀까말까
        repository.insertChatroom(chatroom);
        //TODO: 팀 관련 정보 캐싱 바로 해버려? 알아서 될 듯? 아닌가

        enterChatroom(chatroom.getId(), userId, nickname);
    }

    public void enterChatroom(Long teamId, Long userId, String nickname) {
        ChatroomUser chatroomUser = ChatroomUser.createChatroomUser(teamId, userId, nickname, LocalDateTime.now());
        //TODO: 이미 userId, teamId에 해당하는 멤버가 존재하면?
        repository.insertJpaMemberUser(chatroomUser);

        RedisChatMessage message = new RedisChatMessage(
                chatroomUser.getId(),
                userId,
                "",
                LocalDateTime.now(),
                ChatType.ENTER
        );
        RedisTeamMember teamMember = new RedisTeamMember(chatroomUser.getId(), userId);

        repository.insertRedisMemberUser(teamId, teamMember);
        chatMessageService.broadcastMessage(teamId, message);
    }

    public boolean leaveChatroom(Long userId, Long teamId) {
        Long teamUserId = repository.getTeamUserId(userId, teamId);
        if (teamUserId == null) {
            return false;
        }

        RedisChatMessage message = new RedisChatMessage(teamUserId, userId, "", LocalDateTime.now(), ChatType.LEAVE);
        RedisTeamMember teamMember = new RedisTeamMember(teamUserId, userId);

        repository.removeMember(teamId, teamMember);
        repository.redisDeleteUserChatroom(userId, teamId);
        chatMessageService.broadcastMessage(teamId, message);

        return true;
    }

    public List<ChatroomResponse> getChatroom(Long userId) {
        List<Long> chatroom = repository.redisGetUserChatroom(userId);

        if (chatroom.isEmpty()) {
            //TODO: DB 조회. 레디스에서 소실됐다는 의미임. 조회 후 레디스에도 넣어줘야함
        }
        //TODO: 가져온 목록을 기반으로 채팅방 이름, 프로필 이미지, 마지막 채팅 등 가져오기

        return null;
    }
}
