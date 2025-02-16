package com.bisang.backend.chat.repository.chatroom;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.chat.repository.chatroom.dto.ChatroomTitleProfileDto;
import com.bisang.backend.common.exception.ChatroomException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomRepository {

    private final ChatroomRedisRepository chatroomRedisRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final RedisCacheRepository redisCacheRepository;

    public void redisDeleteUserChatroom(long userId, long teamId) {
        chatroomRedisRepository.deleteUserChatroom(userId, teamId);
    }

    public List<Long> getUserChatroom(long userId) {
        List<Long> userChatroom = chatroomRedisRepository.getUserChatroom(userId);

        if (userChatroom == null || userChatroom.isEmpty()) {
            //TODO: 배치로 메시지 db저장하는거 하고 나면 마지막 메시지 가져와서 그 기준으로 sort 할 수 있을듯..?
            //TODO: 하고나면 레디스에 업데이트 해줘야함, 근데 솔직히 해줘야함?
            userChatroom = chatroomJpaRepository.findAllIdsByUserId(userId);
        }
        return userChatroom;
    }

    public void insertJpaChatroom(Chatroom chatroom) {
        System.out.println("insertChatroom");
        chatroomJpaRepository.save(chatroom);
    }

    public Map<Object, Object> getChatroomInfo(Long chatroomId) {
        System.out.println("chatroomId:" + chatroomId);
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

    public Long getChatroomIdByteamId(Long teamId) {
        return chatroomJpaRepository.findIdByTeamId(teamId);
    }
}
