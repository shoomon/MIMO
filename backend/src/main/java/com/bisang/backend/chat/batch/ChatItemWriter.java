package com.bisang.backend.chat.batch;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.message.ChatMessageJpaRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ChatItemWriter implements ItemWriter<ChatMessage> {

    private final ChatMessageJpaRepository chatMessageRepository;
    private final RedisTemplate<String, RedisChatMessage> redisChatMessageTemplate;

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

                // Redis ZSet에서 삭제
                redisChatMessageTemplate.opsForZSet().remove(redisKey, redisChatMessage);
            } catch (Exception e) {
                // 삭제 실패 로그 출력
                System.err.println("Failed to delete Redis message: " + chatMessage.getId()
                        + " from Redis. Error: " + e.getMessage());
            }
        }
    }

    // ChatMessage → RedisChatMessage 변환
    private RedisChatMessage convertToRedisChatMessage(ChatMessage chatMessage) {

        return new RedisChatMessage(
                chatMessage.getId(),
                chatMessage.getChatroomId(),
                chatMessage.getUserId(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt(),
                chatMessage.getChatType()
        );
    }
}
