package com.bisang.backend.chat.controller;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.domain.request.ChatMessageRequest;
import com.bisang.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chat) {
        if (chatService.isMember(chat.teamUserId(), roomId)) {
          RedisChatMessage redisMessage = new RedisChatMessage(chat.teamUserId(), chat.nickname(), chat.chat(), LocalDateTime.now(), ChatType.MESSAGE);

          chatService.broadcastMessage(roomId, redisMessage, chat.teamName(), chat.chatroomStatus());
        }
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<Object>> getMessages(@PathVariable Long roomId) {
        //TODO: 사용자가 해당 팀에 포함된 유저인지 확인 필요

        List<Object> list = chatService.getMessages(roomId);
        return ResponseEntity.ok().body(list);
    }
}
