package com.bisang.backend.chat.controller.request;

import com.bisang.backend.chat.domain.ChatType;

import java.time.LocalDateTime;

public record GetMessagesRequest(
        Long id,
        //TODO: 이거 온오프라인 처리할 때 필요할거임. 생성자에 넣고 처리해야함
        Long userId,
        Long teamUserId,
        String chat,
        LocalDateTime timestamp,
        ChatType type
) {
}
