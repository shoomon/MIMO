package com.bisang.backend.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final ChatRedisRepository chatRedisRepository;

    public void redisInsertMemberUser(long teamId, long userId) {
        chatRedisRepository.insertMember(teamId, userId);
    }

    public void redisRemoveMember(long teamId, long userId) {
        chatRedisRepository.deleteMember(teamId, userId);
    }
}
