package com.bisang.backend.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bisang.backend.auth.annotation.AuthSimpleUser;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.domain.SimpleUser;
import com.bisang.backend.chat.controller.request.ChatMessageRequest;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.service.ChatMessageService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.ChatAccessInvalidException;
import com.bisang.backend.common.exception.ExceptionCode;
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
            @AuthSimpleUser User user,
            ChatMessageRequest chat
    ) {
        Long userId = user.getId();
        if (chatroomUserService.isMember(roomId, userId, chat.teamUserId())) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    chat.teamUserId(),
                    userId,
                    chat.chat(),
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
            @RequestParam("teamUserId") Long teamUserId,
            @RequestParam("messageId") Long messageId
    ) {
        if (chatroomUserService.isMember(roomId, user.getId(), teamUserId)) {
            List<ChatMessageResponse> list = chatMessageService.getMessages(roomId, messageId);
            return ResponseEntity.ok().body(list);
        }

        throw new ChatAccessInvalidException(ExceptionCode.UNAUTHORIZED_ACCESS);
    }



    //테스트용
    @PostMapping("/test/{roomId}")
    public ResponseEntity<String> sendM(
            @AuthSimpleUser SimpleUser user,
            @PathVariable Long roomId,
            @RequestBody ChatMessageRequest chat) {
        if (chatroomUserService.isMember(roomId, user.userId(), chat.teamUserId())) {
            RedisChatMessage redisMessage = new RedisChatMessage(
                    chat.teamUserId(),
                    user.userId(),
                    chat.chat(),
                    LocalDateTime.now(),
                    ChatType.MESSAGE
            );

            chatMessageService.broadcastMessage(roomId, redisMessage);
            return ResponseEntity.ok().body("전송 성공");
        }

        return ResponseEntity.badRequest().body("전송 실패");
    }
}
