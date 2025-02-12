package com.bisang.backend.chat.service;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;

    public boolean isMember(Long teamId, Long userId) {
        return chatroomUserRepository.isMember(teamId, userId);
    }

    //TODO: 팀쪽에서 변경되면 호출해줘야함
    public void updateNickname(Long userId, Long teamId, String nickname) {
        chatroomUserRepository.updateNickname(userId, teamId, nickname);
    }

    //TODO: 팀쪽에서 변경되면 호출해줘야함
    public void updateProfileUri(Long userId, Long teamId, String profileUri) {
        chatroomUserRepository.updateProfileUri(userId, teamId, profileUri);
    }

    public void updateLastRead(Long id, LocalDateTime lastDateTime, Long lastChatId, Long roomId) {
        chatroomUserRepository.updateLastRead(id, lastDateTime, roomId, lastChatId);
    }
}
