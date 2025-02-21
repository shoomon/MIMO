package com.bisang.backend.chat.repository.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepository {

    private final ChatMessageRedisRepository chatMessageRedisRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    public List<RedisChatMessage> getMessages(
            Long roomId,
            Long messageId,
            String timestamp,
            Double teamEnterScore,
            Long teamEnterChatId
    ) {
        List<RedisChatMessage> messageList
                = chatMessageRedisRepository.getMessages(roomId, messageId, timestamp, teamEnterScore);
        int size = messageList.size();

        if (size < 30) {
            List<RedisChatMessage> newMessageList = getMessagesFromDB(30 - size, roomId, messageId, teamEnterChatId);
            newMessageList.addAll(messageList);
            Collections.reverse(newMessageList);
            return newMessageList;
        }
        Collections.reverse(messageList);
        return messageList;
    }

    private List<RedisChatMessage> getMessagesFromDB(
            int size,
            Long roomId,
            Long messageId,
            Long enterMessageId
    ) {

        if (messageId < 0) {
            messageId = Long.MAX_VALUE;
        }

        List<ChatMessage> messages = chatMessageJpaRepository
                .getMessages(roomId, messageId, enterMessageId);
        List<RedisChatMessage> result = new LinkedList<>();

        int limit = Math.min(size, messages.size());

        for (int i = 0; i < limit; i++) {
            ChatMessage chatMessage = messages.get(i);

            LocalDateTime createdAt = chatMessage.getCreatedAt();
            Long createdAtMilli = createdAt.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
            RedisChatMessage redisChatMessage = new RedisChatMessage(
                    chatMessage.getId(),
                    chatMessage.getChatroomId(),
                    chatMessage.getUserId(),
                    chatMessage.getMessage(),
                    createdAtMilli,
                    chatMessage.getChatType()
            );

            result.add(redisChatMessage);
        }

        return result;
    }

    public Map<String, Object> getLastChat(Long chatroomId) {
        Map<String, Object> result = new HashMap<>();
        RedisChatMessage message = chatMessageRedisRepository.getLastMessage(chatroomId);

        if (message == null) {
            ChatMessage chatMessage = chatMessageJpaRepository.findTopByChatroomIdOrderByIdDesc(chatroomId);

            getResult(result, chatMessage);
            return result;
        }

        result.put("lastChat", message.getChat());
        result.put("lastDatetime", message.getTimestamp());

        return result;
    }

    private static void getResult(Map<String, Object> result, ChatMessage chatMessage) {
        if (chatMessage == null) {
            log.error("메시지가 하나도 존재하지 않습니다. db 문제");
            result.put("lastChat", "임시 채팅");
            result.put("lastDatetime", Instant.now().toEpochMilli());
            return;
        }
        result.put("lastChat", chatMessage.getMessage());

        Long createdAt = chatMessage.getCreatedAt().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
        result.put("lastDatetime", createdAt);
    }

    public Long calculateUnreadCount(Long chatroomId, Double lastReadScore, Long lastChatId) {
        boolean isChatPresent = chatMessageRedisRepository.checkChat(chatroomId, lastReadScore);
        if (isChatPresent) {
            return chatMessageRedisRepository.unreadCount(chatroomId, lastReadScore) - 1;
        }

        Long redisChatCount = chatMessageRedisRepository.countAllChat(chatroomId);
        Long dbChatCount = chatMessageJpaRepository.countByChatroomIdAndIdGreaterThan(chatroomId, lastChatId);

        return redisChatCount + dbChatCount;
    }
}
