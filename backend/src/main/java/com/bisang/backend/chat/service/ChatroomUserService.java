package com.bisang.backend.chat.service;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        return chatroomUserRepository.isMember(teamId, userId, teamUserId);
    }

}
