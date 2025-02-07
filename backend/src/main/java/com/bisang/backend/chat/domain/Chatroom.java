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

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "chatroom"
)
public class Chatroom {

    @Getter
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "chatroom_user_id", nullable = false)
    private Long userId;

    @Column(length = 40, name = "chatroom_title", nullable = false)
    private String title;

    @Column(name = "profile_uri")
    private String profileUri;

    @Enumerated(STRING)
    @Column(name = "chatroom_status", nullable = false)
    private ChatroomStatus status;

    private Chatroom(Long userId, String title, String profileUri, ChatroomStatus status) {
        this.userId = userId;
        this.title = title;
        this.profileUri = profileUri;
        this.status = status;
    }

    public static Chatroom createChatroom(Long userId, String title, String profileUri, ChatroomStatus status) {
        return new Chatroom(userId, title, profileUri, status);
    }
}
