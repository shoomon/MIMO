package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.util.LinkedList;
>>>>>>> f46c218 (feat: 채팅방 멤버 추가 삭제 로직 추가, 사용자 인증 로직 추가 (아직 테스트용))
import java.util.List;
=======
=======
import java.util.List;
>>>>>>> 7a73166 (feat: 채팅 기록 조회 기능 (개발중))
import java.util.Map;
>>>>>>> 70af064 (feat: 유저정보 레디스 캐싱 처리)
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository repository;
    private final SimpMessagingTemplate template;

    /**
     * 새로운 모임에 가입 시 호출
     * @param teamId
     * @param userId
     * @param nickname
     */
    public void enterChatroom(Long teamId, Long userId, String nickname) {
        ChatroomUser chatroomUser = ChatroomUser.createChatroomUser(teamId, userId, nickname, LocalDateTime.now());
        //TODO: 이미 userId, teamId에 해당하는 멤버가 존재하면?
        repository.insertJpaMemberUser(chatroomUser);

        RedisChatMessage message = new RedisChatMessage(chatroomUser.getId(), "", LocalDateTime.now(), ChatType.ENTER);
        RedisTeamMember teamMember = new RedisTeamMember(chatroomUser.getId(), userId);

        repository.insertRedisMemberUser(teamId, teamMember);
        broadcastMessage(teamId, message);
    }

    /**
     * 모임 탈퇴, 강퇴 시 호출
     * @param userId
     * @param teamId
     * @return 유저가 실제로 이 채팅방에 소속되어있는지 확인해줌
     */
    public boolean leaveChatroom(Long userId, Long teamId) {
        Long teamUserId = repository.getTeamUserId(userId, teamId);
        if (teamUserId == null) {
            return false;
        }

        RedisChatMessage message = new RedisChatMessage(teamUserId, "", LocalDateTime.now(), ChatType.LEAVE);
        RedisTeamMember teamMember = new RedisTeamMember(teamUserId, userId);

        repository.removeMember(teamId, teamMember);
        repository.redisDeleteUserChatroom(userId, teamId);
        broadcastMessage(teamId, message);

        return true;
    }

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

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        RedisTeamMember teamMember = new RedisTeamMember(teamUserId, userId);
        return repository.isMember(teamId, teamMember);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public List<Object> getMessages(Long teamId) {
        return repository.redisGetMessages(teamId);
=======
    public List<ChatMessageResponse> getMessages(Long roomId) {
<<<<<<< HEAD
        List<ChatMessageResponse> messageList = repository.getMessages(roomId);
        return messageList;
>>>>>>> 7a73166 (feat: 채팅 기록 조회 기능 (개발중))
=======
        List<RedisChatMessage> messageList = repository.getMessages(roomId);
=======
    public List<ChatMessageResponse> getMessages(Long roomId, Long messageId) {
        List<RedisChatMessage> messageList = repository.getMessages(roomId, messageId);
>>>>>>> fca93df (feat: 채팅 기록 메시지id 기준 100개씩 가져오기 기능 구현)
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
>>>>>>> f46c218 (feat: 채팅방 멤버 추가 삭제 로직 추가, 사용자 인증 로직 추가 (아직 테스트용))
    }
}
