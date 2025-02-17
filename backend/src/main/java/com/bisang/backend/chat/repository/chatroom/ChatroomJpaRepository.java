package com.bisang.backend.chat.repository.chatroom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.chat.domain.Chatroom;
import com.bisang.backend.chat.repository.chatroom.dto.ChatroomTitleProfileDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, Long> {

    @Query("SELECT new com.bisang.backend.chat.repository.chatroom.dto.ChatroomTitleProfileDto(c.title, c.profileUri) "
            + "FROM Chatroom c "
            + "WHERE c.id = :chatroomId")
    Optional<ChatroomTitleProfileDto> findTitleAndProfileUriById(@Param("chatroomId") Long chatroomId);

    @Query("SELECT c.id FROM Chatroom c WHERE c.teamId = :teamId")
    Optional<Long> findIdByTeamId(Long teamId);
}
