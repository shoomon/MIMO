package com.bisang.backend.chat.controller;

import com.bisang.backend.chat.domain.ChatType;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;
import com.bisang.backend.chat.controller.request.ChatMessageRequest;
import com.bisang.backend.chat.controller.response.ChatMessageResponse;
import com.bisang.backend.chat.service.ChatService;
import com.bisang.backend.common.exception.ChatAccessInvalidException;
import com.bisang.backend.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chat) {
        //TODO: SimpleUser? 어노테이션으로 userId 가져와서 아래 if문에 teamUserId를 userId로 바꿔야함
        Long userId = 1L;
        if (chatService.isMember(roomId, userId, chat.teamUserId())) {
          RedisChatMessage redisMessage = new RedisChatMessage(chat.teamUserId(), chat.chat(), LocalDateTime.now(), ChatType.MESSAGE);

          chatService.broadcastMessage(roomId, redisMessage);
        }
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
//            @AuthUser User user,
            @PathVariable("roomId") Long roomId,
            @RequestParam("teamUserId") Long teamUserId,
            @RequestParam("messageId") Long messageId
    ) {
        if (chatService.isMember(roomId, 1L, teamUserId)) {
            List<ChatMessageResponse> list = chatService.getMessages(roomId, messageId);
            return ResponseEntity.ok().body(list);
        }

        throw new ChatAccessInvalidException(ExceptionCode.UNAUTHORIZED_ACCESS);
    }




    //테스트용
    @PostMapping("/test/{roomId}")
    public ResponseEntity<String> sendM(@PathVariable Long roomId, @RequestBody ChatMessageRequest chat) {
        if (chatService.isMember(roomId, 1L, chat.teamUserId())) {
            RedisChatMessage redisMessage = new RedisChatMessage(chat.teamUserId(), chat.chat(), LocalDateTime.now(), ChatType.MESSAGE);

            chatService.broadcastMessage(roomId, redisMessage);
            return ResponseEntity.ok().body("전송 성공");
        }

        return ResponseEntity.badRequest().body("전송 실패");
    }

    @GetMapping("/test/enterChatroom")
    public ResponseEntity<String> enterChatroom(@RequestParam("userId") Long userId, @RequestParam("nickname") String nickname, @RequestParam("teamId") Long teamId) {
        chatService.enterChatroom(teamId, userId, nickname);
        return ResponseEntity.ok().body(nickname + "님 입장");
    }

    @GetMapping("/test/leaveChatroom")
    public ResponseEntity<String> leaveChatroom(@RequestParam("userId") Long userId, @RequestParam("teamId") Long teamId) {
        if (chatService.leaveChatroom(userId, teamId)) {
            return ResponseEntity.ok().body(userId + "번 유저 퇴장");
        }
        return ResponseEntity.badRequest().body("퇴장 실패");
    }
}