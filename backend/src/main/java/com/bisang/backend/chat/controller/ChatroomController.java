package com.bisang.backend.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.request.LastReadRequest;
import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.domain.ChatroomStatus;
import com.bisang.backend.chat.service.ChatroomService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.utils.DateUtils;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final ChatroomUserService chatroomUserService;

    @GetMapping()
    public ResponseEntity<List<ChatroomResponse>> getChatroom(@AuthUser User user) {
        Long userId = user.getId();
        List<ChatroomResponse> list = chatroomService.getChatroom(userId);

        if (list == null) {
            //TODO: 여기 뭐 exception 만들어서 보내줘야하나?
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/last-read")
    public ResponseEntity<String> updateLastRead(
            @AuthUser User user,
            @RequestBody LastReadRequest lastReadRequest
    ) {
        LocalDateTime lastReadDateTime = DateUtils.DateToLocalDateTime(lastReadRequest.lastReadDateTime());

        chatroomUserService.updateLastRead(
                user.getId(),
                lastReadDateTime,
                lastReadRequest.lastReadChatId(),
                lastReadRequest.chatroomId());

        return ResponseEntity.ok().body("업데이트 성공");
    }


    //테스트용

    @GetMapping("/test/create-chatroom")
    public ResponseEntity<String> createChatroom(
            @AuthUser User user,
            @RequestParam("title") String title,
            @RequestParam("teamId") Long teamId
    ) {
        chatroomService.createChatroom(
                user.getId(),
                teamId,
                user.getNickname(),
                title,
                "",
                ChatroomStatus.GROUP
        );
        return ResponseEntity.ok().body(title + " 생성 성공");
    }

    @GetMapping("/test/enter-chatroom")
    public ResponseEntity<String> enterChatroom(
            @RequestParam("userId") Long userId,
            @RequestParam("nickname") String nickname,
            @RequestParam("teamId") Long teamId
    ) {
        chatroomUserService.enterChatroom(teamId, userId, nickname);
        return ResponseEntity.ok().body(nickname + "님 입장");
    }

    @GetMapping("/test/leave-chatroom")
    public ResponseEntity<String> leaveChatroom(
            @RequestParam("userId") Long userId,
            @RequestParam("teamId") Long teamId
    ) {
        if (chatroomUserService.leaveChatroom(userId, teamId)) {
            return ResponseEntity.ok().body(userId + "번 유저 퇴장");
        }
        return ResponseEntity.badRequest().body("퇴장 실패");
    }

    @GetMapping("/test/force-leave")
    public ResponseEntity<String> forceLeave(
            @RequestParam("userId") Long userId,
            @RequestParam("teamId") Long teamId
    ) {
        chatroomUserService.forceLeave(teamId, userId);
        return ResponseEntity.ok().body(userId + "번 유저 강퇴");
    }
}
