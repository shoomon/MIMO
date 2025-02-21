package com.bisang.backend.chat.batch;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.message.ChatMessageJpaRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ChatItemWriter implements ItemWriter<ChatMessage> {

    private final ChatMessageJpaRepository chatMessageRepository;
    private final StringRedisTemplate redisChatMessageTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void write(Chunk<? extends ChatMessage> chunk) {
        // DB 저장
        chatMessageRepository.saveAll(chunk.getItems());

        // Redis에서 삭제
        deleteFromRedis(chunk);
    }

    private void deleteFromRedis(Chunk<? extends ChatMessage> chunk) {
        for (ChatMessage chatMessage : chunk.getItems()) {
            try {
                String redisKey = "teamMessage:" + chatMessage.getChatroomId();
                RedisChatMessage redisChatMessage = convertToRedisChatMessage(chatMessage);
                String messageJson = objectMapper.writeValueAsString(redisChatMessage);
                // Redis ZSet에서 삭제
                redisChatMessageTemplate.opsForZSet().remove(redisKey, messageJson);
            } catch (Exception e) {
                // 삭제 실패 로그 출력
                System.err.println("Failed to delete Redis message: " + chatMessage.getId()
                        + " from Redis. Error: " + e.getMessage());
            }
        }
    }

    // ChatMessage → RedisChatMessage 변환
    private RedisChatMessage convertToRedisChatMessage(ChatMessage chatMessage) {
        LocalDateTime createdAt = chatMessage.getCreatedAt();
        Long createdAtMilli = createdAt.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

        return new RedisChatMessage(
                chatMessage.getId(),
                chatMessage.getChatroomId(),
                chatMessage.getUserId(),
                chatMessage.getMessage(),
                createdAtMilli,
                chatMessage.getChatType()
        );
    }
}
