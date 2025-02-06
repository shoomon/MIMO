package com.bisang.backend.chat.service;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRepository repository;
    private final ChatMessageService chatMessageService;

    public void enterChatroom(Long teamId, Long userId, String nickname) {
        ChatroomUser chatroomUser = ChatroomUser.createChatroomUser(teamId, userId, nickname, LocalDateTime.now());
        //TODO: 이미 userId, teamId에 해당하는 멤버가 존재하면?
        repository.insertJpaMemberUser(chatroomUser);

        RedisChatMessage message = new RedisChatMessage(chatroomUser.getId(), "", LocalDateTime.now(), ChatType.ENTER);
        RedisTeamMember teamMember = new RedisTeamMember(chatroomUser.getId(), userId);

        repository.insertRedisMemberUser(teamId, teamMember);
        chatMessageService.broadcastMessage(teamId, message);
    }

    public boolean leaveChatroom(Long userId, Long teamId) {
        Long teamUserId = repository.getTeamUserId(userId, teamId);
        if (teamUserId == null) {
            return false;
        }

        RedisChatMessage message = new RedisChatMessage(teamUserId, "", LocalDateTime.now(), ChatType.LEAVE);
        RedisTeamMember teamMember = new RedisTeamMember(teamUserId, userId);

        repository.removeMember(teamId, teamMember);
        repository.redisDeleteUserChatroom(userId, teamId);
        chatMessageService.broadcastMessage(teamId, message);

        return true;
    }

    public List<ChatroomResponse> getChatroom(Long userId) {
        //TODO: 채팅방 목록 조회 - 레디스에서 조회해오고 없으면 db가서 가져와야함

        //TODO: 가져온 목록을 기반으로 채팅방 이름, 프로필 이미지, 마지막 채팅 등 가져오기

        return null;
    }
}
