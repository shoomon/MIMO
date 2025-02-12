package com.bisang.backend.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.request.ChatMessageRequest;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.service.ChatMessageService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.ChatAccessInvalidException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.utils.DateUtils;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat-message")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatroomUserService chatroomUserService;

    @MessageMapping("/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            SimpMessageHeaderAccessor headerAccessor,
            ChatMessageRequest chat
    ) {
        Long userId = (Long)headerAccessor.getSessionAttributes().get("userId");

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
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @AuthUser User user,
            @PathVariable("roomId") Long roomId,
            @RequestParam("messageId") Long messageId,
            @RequestParam("timestamp") String timestamp
    ) {
        if (chatroomUserService.isMember(roomId, user.getId())) {
            LocalDateTime datetime = DateUtils.DateToLocalDateTime(timestamp);
            List<ChatMessageResponse> list = chatMessageService.getMessages(roomId, messageId, datetime);

            return ResponseEntity.ok().body(list);
        }

        throw new ChatAccessInvalidException(ExceptionCode.UNAUTHORIZED_ACCESS);
    }



    //테스트용
    @PostMapping("/test/{roomId}")
    public ResponseEntity<String> sendM(
            @PathVariable Long roomId,
            @RequestBody ChatMessageRequest chat
    ) {
        if (chatroomUserService.isMember(roomId, 1L)) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    roomId,
                    1L,
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
