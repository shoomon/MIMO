package com.bisang.backend.chat.repository.chatroomRepository;

import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatroomRepository {

    private final ChatroomRedisRepository chatroomRedisRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

    public void redisUpdateUserChatroom(long userId, long teamId, Double timestamp) {
        chatroomRedisRepository.updateUserChatroom(userId, teamId, timestamp);
    }

    public void redisDeleteUserChatroom(long userId, long teamId) {
        chatroomRedisRepository.deleteUserChatroom(userId, teamId);
    }

    public List<Long> getUserChatroom(long userId) {
        //TODO: 레디스에 없으면 DB 조회
        return chatroomRedisRepository.getUserChatroom(userId);
    }

    public void insertChatroom(Chatroom chatroom) {
        chatroomJpaRepository.save(chatroom);
    }

    public List<ChatroomResponse> getChatroom(Long userId) {
        //TODO: 만들기

        return null;
    }

    public Map<Object, Object> getChatroomInfo(Long chatroomId) {
        Map<Object, Object> chatroomInfo = redisCacheRepository.getChatroomInfo(chatroomId);
        if (chatroomInfo.isEmpty()) {
            Object[] info = chatroomJpaRepository.findTitleAndProfileUriById(chatroomId);
            chatroomInfo.put("title", info[0]);
            chatroomInfo.put("profileUri", info[1]);

            redisCacheRepository.cacheChatroomInfo(chatroomId, (String)info[0], (String)info[1], ChatroomStatus.GROUP);
        }

        return chatroomInfo;
    }

}
