package com.bisang.backend.chat.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.service.ChatroomService;
import com.bisang.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @GetMapping()
    public ResponseEntity<List<ChatroomResponse>> getChatroom(@AuthUser User user) {
        Long userId = user.getId();
        List<ChatroomResponse> list = chatroomService.getChatroom(userId);

        return ResponseEntity.ok().body(list);
    }



    //테스트용
    @GetMapping("/test/enterChatroom")
    public ResponseEntity<String> enterChatroom(@RequestParam("userId") Long userId, @RequestParam("nickname") String nickname, @RequestParam("teamId") Long teamId) {
        chatroomService.enterChatroom(teamId, userId, nickname);
        return ResponseEntity.ok().body(nickname + "님 입장");
    }

    @GetMapping("/test/leaveChatroom")
    public ResponseEntity<String> leaveChatroom(@RequestParam("userId") Long userId, @RequestParam("teamId") Long teamId) {
        if (chatroomService.leaveChatroom(userId, teamId)) {
            return ResponseEntity.ok().body(userId + "번 유저 퇴장");
        }
        return ResponseEntity.badRequest().body("퇴장 실패");
    }
}
