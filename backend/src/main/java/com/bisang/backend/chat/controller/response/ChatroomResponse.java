package com.bisang.backend.chat.controller.response;

import java.time.LocalDateTime;

public record ChatroomResponse(
    Long chatroomId,
    String chatroomName,
    String chatroomImage,
    String lastChat,
    LocalDateTime lastDateTime,
    String nickname,
    Long unreadCount
) {
}
