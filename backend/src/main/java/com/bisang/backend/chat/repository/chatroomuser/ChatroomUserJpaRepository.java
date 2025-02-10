package com.bisang.backend.chat.repository.chatroomuser;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.chat.domain.ChatroomUser;

import io.lettuce.core.dynamic.annotation.Param;

public interface ChatroomUserJpaRepository extends JpaRepository<ChatroomUser, Long> {
    @Query("SELECT u.userId FROM ChatroomUser u WHERE u.chatroomId = :teamId")
    Set<Long> findUserIdsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT u.nickname FROM ChatroomUser u WHERE u.id = :id")
    String findNicknameById(@Param("id") Long id);

    @Query("SELECT u.id FROM ChatroomUser u WHERE u.userId = :userId AND u.chatroomId = :teamId")
    Long findTeamUserIdByUserIdAndChatroomId(@Param("userId") Long userId, @Param("teamId") Long teamId);

    boolean existsByIdAndUserIdAndChatroomId(Long teamUserId, Long userId, Long chatroomId);
}
