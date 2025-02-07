package com.bisang.backend.chat.service;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import com.bisang.backend.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatRepository repository;

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        return repository.isMember(teamId, userId, teamUserId);
    }

}
