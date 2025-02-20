package com.bisang.backend.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Redis 키 Prefix
    private static final String MESSAGE_ID_KEY    = "chat:message:id"; // 메시지 ID 증가용
    private static final String USER_CHATROOM_KEY = "userChatroom:";   // userChatroom:<userId>
    private static final String TEAM_MESSAGE_KEY  = "teamMessage:";    // teamMessage:<chatroomId>
    private static final String teamMemberKey = "teamMember:";
    private static final String lastReadScoreKey = "lastReadScore:";
    private static final String lastReadIdKey = "lastReadId:";


    private static final DefaultRedisScript<Long> SEND_MESSAGE_SCRIPT = new DefaultRedisScript<>(
            // Lua 스크립트 시작
            "local messageId = redis.call('INCR', KEYS[1])\n"
                    + "local timestamp = tonumber(ARGV[1])\n"
                    + "local chatroomId = ARGV[2]\n"
                    + "local messageJson = ARGV[3]\n"
                    + "\n"
                    + "-- 스코어 계산: millisecond + messageId의 일부 소수점 덧붙임\n"
                    + "local score = timestamp + (messageId % 1000) / 1000.0\n"
                    + "\n"
                    + "-- messageJson 내 \"id\":null -> \"id\":messageId 로 치환\n"
                    + "local messageJsonWithId = messageJson:gsub('\"id\":null', '\"id\":' .. messageId)\n"
                    + "\n"
                    + "-- (1) userChatroom:<userId> ZSET에 chatroomId 추가 (score 업데이트)\n"
                    + "for i = 4, #ARGV do\n"
                    + "    local userId = ARGV[i]\n"
                    + "    local userChatroomKey = KEYS[2] .. userId\n"
                    + "    -- 이미 존재하더라도 오류 처리 없이 score 업데이트\n"
                    + "    redis.call('ZADD', userChatroomKey, score, chatroomId)\n"
                    + "end\n"
                    + "\n"
                    + "-- (2) teamMessage:<chatroomId> ZSET에 메시지 JSON 추가\n"
                    + "local teamMessageKey = KEYS[3] .. chatroomId\n"
                    + "redis.call('ZADD', teamMessageKey, score, messageJsonWithId)\n"
                    + "\n"
                    + "-- (3) 반환: 새로 발급된 messageId\n"
                    + "return messageId\n",
            Long.class
    );

    public void afterSendMessage(Long chatroomId, RedisChatMessage message) {
        Set<Long> teamMembers = chatroomUserRepository.getTeamMembers(chatroomId);

        double timestamp = (double) message.getTimestamp();

        message.setId(null);

        String messageJson;
        try {
            messageJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Message serialization failed", e);
        }

        List<String> keys = List.of(
                MESSAGE_ID_KEY,
                USER_CHATROOM_KEY,
                TEAM_MESSAGE_KEY
        );

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(timestamp));
        args.add(String.valueOf(chatroomId));
        args.add(messageJson);

        for (Long userId : teamMembers) {
            args.add(String.valueOf(userId));
        }

        Long result = stringRedisTemplate.execute(
                SEND_MESSAGE_SCRIPT,
                keys,
                args.toArray(new String[0])
        );

        if (result == null) {
            throw new RuntimeException("Failed to send message (null result).");
        }

        message.setId(result);
    }

    public void afterEnterChatroom(Long chatroomId, Long userId, LocalDateTime lastDateTime, Long lastChatId) {
        double score = lastDateTime
                .toInstant(ZoneOffset.ofTotalSeconds(0))
                .toEpochMilli() + (lastChatId % 1000) / 1000.0;

        // Lua 스크립트 내용
        String luaScript =
                "redis.call('SADD', KEYS[1], ARGV[1]);"
                        + "redis.call('SET', KEYS[2], ARGV[2]);"
                        + "redis.call('SET', KEYS[3], ARGV[3]);"
                        + "return 1;";

        // 실행할 RedisScript 객체 생성 (반환 타입은 Long 예시)
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);

        // 실제 Redis에 넘길 key 목록
        String teamMemberSetKey = teamMemberKey + chatroomId;
        String readScoreKey = lastReadScoreKey + chatroomId + ":" + userId;
        String readIdKey = lastReadIdKey + chatroomId + ":" + userId;

        Long result = stringRedisTemplate.execute(
                redisScript,
                Arrays.asList(teamMemberSetKey, readScoreKey, readIdKey), // KEYS
                userId, String.valueOf(score), String.valueOf(lastChatId) // ARGV
        );

        if (result == null || result != 1L) {
            throw new RuntimeException("Failed to save teamMember");
        }
    }

}
