package com.bisang.backend.chat.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "chatroom"
)
public class Chatroom {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "chatroom_user_id", nullable = false)
    private Long userId;

    @Column(length = 40, name = "chatroom_title", nullable = false)
    private String title;

    @Enumerated(STRING)
    @Column(name = "chatroom_status", nullable = false)
    private ChatroomStatus status;

    private Chatroom(Long userId, String title, ChatroomStatus status) {
        this.userId = userId;
        this.title = title;
        this.status = status;
    }
}
