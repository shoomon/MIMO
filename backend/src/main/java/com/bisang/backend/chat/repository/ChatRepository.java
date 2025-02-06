package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.ChatroomUser;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final ChatRedisRepository chatRedisRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

    public void insertRedisMemberUser(long teamId, RedisTeamMember teamMember) {
        chatRedisRepository.insertMember(teamId, teamMember);
    }

    public void insertJpaMemberUser(ChatroomUser chatroomUser) {
        chatroomUserJpaRepository.save(chatroomUser);
    }

    public void removeMember(long teamId, RedisTeamMember teamMember) {
        chatRedisRepository.deleteMember(teamId, teamMember);
        chatroomUserJpaRepository.deleteById(teamMember.getTeamUserId());
    }

    public Set<Long> getTeamMembers(long teamId) {
        Set<Long> teamMembers = chatRedisRepository.getTeamMembers(teamId);
        if (teamMembers.isEmpty()) {
            teamMembers = chatroomUserJpaRepository.findUserIdsByTeamId(teamId);
        }
        return teamMembers;
    }

    public void redisUpdateUserChatroom(long userId, long teamId, Double timestamp) {
        chatRedisRepository.updateUserChatroom(userId, teamId, timestamp);
    }

    public void redisDeleteUserChatroom(long userId, long teamId) {
        chatRedisRepository.deleteUserChatroom(userId, teamId);
    }

    public void redisSaveMessage(long teamId, RedisChatMessage message) {
        chatRedisRepository.saveMessage(teamId, message);
    }

    public boolean isMember(Long teamId, RedisTeamMember teamMember) {
        if (chatRedisRepository.isMember(teamId, teamMember)) {
            return true;
        }

        if (chatroomUserJpaRepository.existsByIdAndUserIdAndChatroomId(teamMember.getTeamUserId(), teamMember.getUserId(), teamId)) {
            chatRedisRepository.insertMember(teamId, teamMember);
            return true;
        }

        return false;
    }

    public Map<Object, Object> getUserInfo(Long teamUserId, Long userId) {
        Map<Object, Object> userInfo = redisCacheRepository.getUserProfile(teamUserId);
        if (userInfo.isEmpty()) {
            String nickname = chatroomUserJpaRepository.findNicknameById(teamUserId);
            //TODO: 유저쪽에서 프로필 이미지 받아오는 메서드 받아와서 넣어야함
            String profileImage = "";

            redisCacheRepository.cacheUserProfile(teamUserId, nickname, profileImage);

            userInfo.put("name", nickname);
            userInfo.put("profileImage", profileImage);
        }

        return userInfo;
    }

    public Long getTeamUserId(Long userId, Long teamId) {
        return chatroomUserJpaRepository.findTeamUserIdByUserIdAndChatroomId(userId, teamId);
    }

    public List<RedisChatMessage> getMessages(Long roomId, Long messageId) {
        List<RedisChatMessage> messageList = chatRedisRepository.getMessages(roomId, messageId);
        int size = messageList.size();
        System.out.println(size);
        if (size < 100) {
            List<RedisChatMessage> newMessageList = getMessagesFromDB(100-size, roomId, messageId);
            newMessageList.addAll(messageList);
            return newMessageList;
        }
        return messageList;
    }

    private List<RedisChatMessage> getMessagesFromDB(int size, Long roomId, Long messageId) {
        List<ChatMessage> messages = chatMessageJpaRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(roomId, messageId);
        List<RedisChatMessage> result = new LinkedList<>();

        int limit = Math.min(size, messages.size());

        for (int i = limit - 1; i >= 0; i--) {
            ChatMessage chatMessage = messages.get(i);

            RedisChatMessage redisChatMessage = new RedisChatMessage(
                    chatMessage.getId(),
                    chatMessage.getTeamUserId(),
                    chatMessage.getMessage(),
                    chatMessage.getCreatedAt(),
                    ChatType.MESSAGE
            );

            result.add(redisChatMessage);
        }

        return result;
    }

}
