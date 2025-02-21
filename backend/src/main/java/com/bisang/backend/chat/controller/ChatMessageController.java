package com.bisang.backend.chat.controller;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.request.ChatMessageRequest;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.service.ChatMessageService;
import com.bisang.backend.chat.service.ChatRedisService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.ChatAccessInvalidException;
import com.bisang.backend.common.exception.UnauthorizedChatException;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat-message")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatroomUserService chatroomUserService;
    private final JwtUtil jwtUtil;
    private final ChatRedisService chatRedisService;

    @MessageMapping("/chat-message/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Header("Authorization") String token,
            ChatMessageRequest chat
    ) {
        if (!jwtUtil.isAccessTokenValid(token)) {
            throw new UnauthorizedChatException(UNAUTHORIZED_USER);
        }

        Long userId = Long.valueOf(jwtUtil.getSubject(token));
        Long currentTime = Instant.now().toEpochMilli();

        if (chatroomUserService.isMember(roomId, userId)) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    roomId,
                    userId,
                    chat.message(),
                    currentTime,
                    ChatType.MESSAGE
            );
            chatRedisService.afterSendMessage(roomId, redisMessage);
            chatMessageService.broadcastMessage(roomId, redisMessage);

            return;
        }
        throw new UnauthorizedChatException(NOT_FOUND_TEAM_USER);
    }

    @MessageExceptionHandler(UnauthorizedChatException.class)
    @SendToUser("/queue/errors")
    private UnauthorizedChatException handleInvalidTokenException() {
        return new UnauthorizedChatException(UNAUTHORIZED_ACCESS);
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @AuthUser User user,
            @PathVariable("roomId") Long roomId,
            @RequestParam("messageId") Long messageId,
            @RequestParam("timestamp") String timestamp
    ) {
        if (chatroomUserService.isMember(roomId, user.getId())) {
            List<ChatMessageResponse> list = chatMessageService.getMessages(user.getId(), roomId, messageId, timestamp);

            return ResponseEntity.ok().body(list);
        }
        throw new ChatAccessInvalidException(UNAUTHORIZED_ACCESS);
    }
}
