package com.bisang.backend.chat.repository.chatMessageRepository;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final ChatMessageRedisRepository chatMessageRedisRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    public void redisSaveMessage(long teamId, RedisChatMessage message) {
        chatMessageRedisRepository.saveMessage(teamId, message);
    }

    public List<RedisChatMessage> getMessages(Long roomId, Long messageId) {
        List<RedisChatMessage> messageList = chatMessageRedisRepository.getMessages(roomId, messageId);
        int size = messageList.size();
        System.out.println(size);
        if (size < 30) {
            List<RedisChatMessage> newMessageList = getMessagesFromDB(30 - size, roomId, messageId);
            newMessageList.addAll(messageList);
            return newMessageList;
        }
        return messageList;
    }

    private List<RedisChatMessage> getMessagesFromDB(
            int size,
            Long roomId,
            Long messageId
    ) {
        List<ChatMessage> messages = chatMessageJpaRepository
                .findByChatroomIdAndIdLessThanOrderByIdDesc(roomId, messageId);
        List<RedisChatMessage> result = new LinkedList<>();

        int limit = Math.min(size, messages.size());

        for (int i = limit - 1; i >= 0; i--) {
            ChatMessage chatMessage = messages.get(i);

            RedisChatMessage redisChatMessage = new RedisChatMessage(
                    chatMessage.getId(),
                    chatMessage.getUserId(),
                    chatMessage.getTeamUserId(),
                    chatMessage.getMessage(),
                    chatMessage.getCreatedAt(),
                    chatMessage.getChatType()
            );

            result.add(redisChatMessage);
        }

        return result;
    }

    public void getLastChat(Long chatroomId) {
        RedisChatMessage message = chatMessageRedisRepository.getLastMessage(chatroomId);

        if (message == null) {
            //TODO: db에서 가져오기
            ChatMessage chatMessage = chatMessageJpaRepository.findTopByChatroomIdOrderByIdDesc(chatroomId);

        }
    }
}
