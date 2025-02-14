package com.bisang.backend.chat.domain.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.bisang.backend.chat.domain.ChatType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RedisChatMessage implements Serializable {

    @Setter
    private Long id;

    private Long chatroomId;

    private Long userId;

    private String chat;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    private ChatType type;

    public RedisChatMessage(
            Long chatroomId,
            Long userId,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }

    public RedisChatMessage(
            Long id,
            Long chatroomId,
            Long userId,
            String chat,
            LocalDateTime timestamp,
            ChatType type
    ) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.chat = chat;
        this.timestamp = timestamp;
        this.type = type;
    }
}
