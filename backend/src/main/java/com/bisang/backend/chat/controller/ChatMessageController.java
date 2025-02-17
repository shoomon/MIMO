package com.bisang.backend.chat.controller;

import static com.bisang.backend.common.exception.ExceptionCode.*;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.request.ChatMessageRequest;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.service.ChatMessageService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.AccountException;
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
        if (chatroomUserService.isMember(roomId, userId)) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    roomId,
                    userId,
                    chat.message(),
                    LocalDateTime.now(),
                    ChatType.MESSAGE
            );

            chatMessageService.broadcastMessage(roomId, redisMessage);
        }

        throw new UnauthorizedChatException(NOT_FOUND_TEAM_USER);
    }

    @MessageExceptionHandler(AccountException.class)
    @SendToUser("/queue/errors")
    private AccountException handleInvalidTokenException() {
        return new AccountException(UNAUTHORIZED_ACCESS);
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @AuthUser User user,
            @PathVariable("roomId") Long roomId,
            @RequestParam("messageId") Long messageId,
            @RequestParam("timestamp") String timestamp
    ) {
        if (chatroomUserService.isMember(roomId, user.getId())) {
            List<ChatMessageResponse> list = chatMessageService.getMessages(roomId, messageId, timestamp);

            return ResponseEntity.ok().body(list);
        }

        throw new ChatAccessInvalidException(UNAUTHORIZED_ACCESS);
    }



    //테스트용
    @PostMapping("/test/{roomId}")
    public ResponseEntity<String> sendM(
            @PathVariable Long roomId,
            @RequestBody ChatMessageRequest chat
    ) {
        if (chatroomUserService.isMember(roomId, 11L)) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    roomId,
                    11L,
                    chat.message(),
                    LocalDateTime.now(),
                    ChatType.MESSAGE
            );

            chatMessageService.broadcastMessage(roomId, redisMessage);
            return ResponseEntity.ok().body("전송 성공");
        }

        return ResponseEntity.badRequest().body("전송 실패");
    }
}
