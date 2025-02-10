package com.bisang.backend.chat.service;

import org.springframework.stereotype.Service;

import com.bisang.backend.chat.repository.RedisCacheRepository;
import com.bisang.backend.chat.repository.chatroomuser.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final RedisCacheRepository redisCacheRepository;

    public boolean isMember(Long teamId, Long userId, Long teamUserId) {
        return chatroomUserRepository.isMember(teamId, userId, teamUserId);
    }

    //TODO: 팀쪽에서 변경되면 호출해줘야함
    public void updateNickname(Long userId, Long teamId, String nickname) {
        chatroomUserRepository.updateNickname(userId, teamId, nickname);
    }

    //TODO: 팀쪽에서 변경되면 호출해줘야함
    public void updateProfileUri(Long userId, Long teamId, String profileUri) {
        chatroomUserRepository.updateProfileUri(userId, teamId, profileUri);
    }

}
