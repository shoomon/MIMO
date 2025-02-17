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
import lombok.Setter;

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

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Setter
    @Column(length = 40, name = "chatroom_title", nullable = false)
    private String title;

    @Setter
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

    private Chatroom(Long userId, Long teamId, String title, String profileUri, ChatroomStatus status) {
        this.userId = userId;
        this.teamId = teamId;
        this.title = title;
        this.profileUri = profileUri;
        this.status = status;
    }

    public static Chatroom createChatroom(Long userId, String title, String profileUri, ChatroomStatus status) {
        return new Chatroom(userId, title, profileUri, status);
    }

    public static Chatroom createTeamChatroom(
            Long userId,
            Long teamId,
            String title,
            String profileUri,
            ChatroomStatus status
    ) {
        return new Chatroom(userId, teamId, title, profileUri, status);
    }

}
