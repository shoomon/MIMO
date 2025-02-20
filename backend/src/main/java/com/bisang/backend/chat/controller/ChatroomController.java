package com.bisang.backend.chat.controller;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.UNAUTHORIZED_USER;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.chat.controller.request.LastReadRequest;
import com.bisang.backend.chat.controller.response.ChatroomResponse;
import com.bisang.backend.chat.service.ChatroomService;
import com.bisang.backend.chat.service.ChatroomUserService;
import com.bisang.backend.common.exception.ChatAccessInvalidException;
import com.bisang.backend.common.exception.ChatroomException;
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/last-read")
    public ResponseEntity<String> updateLastRead(
            @AuthUser User user,
            @RequestBody LastReadRequest lastReadRequest
    ) {

        LocalDateTime lastReadDateTime;
        try {
            lastReadDateTime = DateUtils.dateToLocalDateTime(lastReadRequest.lastReadDateTime());
        } catch (NullPointerException | DateTimeParseException e) {
            throw new ChatroomException(INVALID_REQUEST);
        }

        Long lastReadDateTimeMilli = lastReadDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

        if (!chatroomUserService.isMember(lastReadRequest.chatroomId(), user.getId())) {
            throw new ChatAccessInvalidException(UNAUTHORIZED_USER);
        }
        chatroomUserService.updateLastRead(
                user.getId(),
                lastReadDateTimeMilli,
                lastReadRequest.lastReadChatId(),
                lastReadRequest.chatroomId());

        return ResponseEntity.ok().body("업데이트 성공");
    }
}
