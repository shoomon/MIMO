package com.bisang.backend.chat.repository.chatroom;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.chat.repository.chatroom.dto.ChatroomTitleProfileDto;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserJpaRepository;
import com.bisang.backend.common.exception.ChatroomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatroomRepository {

    private final ChatroomRedisRepository chatroomRedisRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final RedisCacheRepository redisCacheRepository;
    private final ChatroomUserJpaRepository chatroomUserJpaRepository;

    public void redisDeleteUserChatroom(long userId, long chatroomId) {
        chatroomRedisRepository.deleteUserChatroom(userId, chatroomId);
    }

    public List<Long> getUserChatroom(long userId) {
        List<Long> userChatroom = chatroomRedisRepository.getUserChatroom(userId);

        if (userChatroom == null || userChatroom.isEmpty()) {
            log.warn("채팅방 실시간 순서가 유실되었을 수 있습니다.");
            userChatroom = chatroomUserJpaRepository.findAllChatroomIdsByUserId(userId);
        }
        return userChatroom;
    }

    public void insertJpaChatroom(Chatroom chatroom) {
        chatroomJpaRepository.save(chatroom);
    }

    public Map<Object, Object> getChatroomInfo(Long chatroomId) {
        Map<Object, Object> chatroomInfo = redisCacheRepository.getChatroomInfo(chatroomId);
        if (chatroomInfo.isEmpty()) {
            ChatroomTitleProfileDto info = chatroomJpaRepository
                    .findTitleAndProfileUriById(chatroomId)
                    .orElseThrow(()->new ChatroomException(NOT_FOUND_TEAM));

            chatroomInfo.put("title", info.getTitle());
            chatroomInfo.put("profileUri", info.getProfileUri());

            redisCacheRepository.cacheChatroomInfo(
                    chatroomId,
                    info.getTitle(),
                    info.getProfileUri(),
                    ChatroomStatus.GROUP
            );
        }

        return chatroomInfo;
    }

    public void updateChatroomProfileUri(Long chatroomId, String profileUri) {

        String originalProfileUri = redisCacheRepository.getChatroomProfileUri(chatroomId);
        redisCacheRepository.updateChatroomProfileUri(chatroomId, profileUri);

        try {
            Chatroom chatroom = chatroomJpaRepository
                    .findById(chatroomId)
                    .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));
            chatroom.setProfileUri(profileUri);
        } catch (ChatroomException e) {
            redisCacheRepository.updateChatroomProfileUri(chatroomId, originalProfileUri);
            throw e;
        }
    }

    public void updateChatroomTitle(Long chatroomId, String chatroomTitle) {
        String originalChatroomTitle = redisCacheRepository.getChatroomTitle(chatroomId);
        redisCacheRepository.updateChatroomTitle(chatroomId, chatroomTitle);

        try {
            Chatroom chatroom = chatroomJpaRepository
                    .findById(chatroomId)
                    .orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));
            chatroom.setTitle(chatroomTitle);
        } catch (ChatroomException e) {
            redisCacheRepository.updateChatroomTitle(chatroomId, originalChatroomTitle);
            throw e;
        }
    }

    public Long getChatroomIdByteamId(Long teamId) {
        return chatroomJpaRepository.findIdByTeamId(teamId).orElseThrow(() -> new ChatroomException(NOT_FOUND_TEAM));
    }
}
