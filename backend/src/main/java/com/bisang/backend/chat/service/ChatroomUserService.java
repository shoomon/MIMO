package com.bisang.backend.chat.service;

import com.bisang.backend.chat.repository.chatMessageRepository.ChatMessageRepository;
import com.bisang.backend.chat.repository.chatroomRepository.ChatroomRepository;
import com.bisang.backend.chat.repository.chatroomUserRepository.ChatroomUserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        return chatroomUserRepository.isMember(teamId, userId, teamUserId);
    }

}
