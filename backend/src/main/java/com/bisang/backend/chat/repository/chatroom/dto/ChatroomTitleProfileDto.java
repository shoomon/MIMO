package com.bisang.backend.chat.repository.chatroom.dto;

public class ChatroomTitleProfileDto {

    private String title;
    private String profileUri;

    public ChatroomTitleProfileDto(String title, String profileUri) {
        this.title = title;
        this.profileUri = profileUri;
    }

    public String getTitle() {
        return title;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
