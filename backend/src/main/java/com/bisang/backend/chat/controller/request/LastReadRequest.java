package com.bisang.backend.chat.controller.request;

import java.util.Date;

public record LastReadRequest(
    String lastReadDateTime,
    Long lastReadChatId,
    Long chatroomId
) {
}
