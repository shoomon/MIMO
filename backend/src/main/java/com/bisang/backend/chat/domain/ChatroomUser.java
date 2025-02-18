package com.bisang.backend.chat.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "chatroom_user",
        indexes = {
            @Index(name = "idx_user_id_last_modified_at", columnList = "user_id, last_modified_at"),
            @Index(name = "idx_user_id_chatroom_id", columnList = "user_id, chatroom_id")
        }
)
public class ChatroomUser {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chatroom_user_id")
    @Getter
    private Long id;

    @Column(name = "chatroom_id", nullable = false)
    private Long chatroomId;

    @Getter
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Setter
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Getter
    @Column(name = "enter_date")
    private LocalDateTime enterDate;

    @Getter
    @Column(name = "enter_chat_id")
    private Long enterChatId;

    private ChatroomUser(
            Long chatroomId,
            Long userId,
            String nickname,
            LocalDateTime enterDate,
            Long enterChatId
    ) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.nickname = nickname;
        this.enterDate = enterDate;
        this.enterChatId = enterChatId;
    }

    public static ChatroomUser createChatroomUser(
            Long chatroomId,
            Long userId,
            String nickname,
            LocalDateTime enterDate,
            Long enterChatId
    ) {
        return new ChatroomUser(chatroomId, userId, nickname, enterDate, enterChatId);
    }

}
