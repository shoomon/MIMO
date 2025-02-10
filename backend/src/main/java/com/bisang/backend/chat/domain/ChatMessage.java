package com.bisang.backend.chat.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "chat_message",
        indexes = {
            @Index(name = "idx_chatroom_id", columnList = "chatroom_id, chat_message_id")
        }
)
public class ChatMessage {

    @Id
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "chatroom_id", nullable = false)
    private Long chatroomId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_user_id")
    private Long teamUserId;

    @Column(length = 220, name = "message")
    private String message;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    private ChatType chatType;

    private ChatMessage(
            Long chatroomId,
            Long userId,
            Long teamUserId,
            String message
    ) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.teamUserId = teamUserId;
        this.message = message;
    }

    public ChatMessage createUserMessage(Long chatroomId, Long userId, String message) {
        return new ChatMessage(chatroomId, userId, null, message);
    }

    public ChatMessage createTeamMessage(Long chatroomId, Long teamUserId, String message) {
        return new ChatMessage(chatroomId, null, teamUserId, message);
    }
}
