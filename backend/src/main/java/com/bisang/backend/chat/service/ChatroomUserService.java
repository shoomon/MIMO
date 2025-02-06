package com.bisang.backend.chat.service;

import com.bisang.backend.chat.domain.redis.RedisTeamMember;
import com.bisang.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatRepository repository;

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        RedisTeamMember teamMember = new RedisTeamMember(teamUserId, userId);
        return repository.isMember(teamId, teamMember);
    }

}
