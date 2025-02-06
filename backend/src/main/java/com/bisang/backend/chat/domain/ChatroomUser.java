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


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "chatroom_user",
        indexes = {
            @Index(name = "idx_user_id_chatroom_id", columnList = "user_id, last_modified_at"),
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

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    private ChatroomUser(
      Long chatroomId,
      Long userId,
      String nickname,
      LocalDateTime lastModifiedAt
    ) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.nickname = nickname;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static ChatroomUser createChatroomUser(Long chatroomId, Long userId, String nickname, LocalDateTime lastModifiedAt) {
        return new ChatroomUser(chatroomId, userId, nickname, lastModifiedAt);
    }

}
