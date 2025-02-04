package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.ChatroomUser;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ChatroomUserJpaRepository extends JpaRepository<ChatroomUser, Long> {
    @Query("SELECT u.userId FROM ChatroomUser u WHERE u.chatroomId = :teamId")
    Set<Long> findUserIdsByTeamId(@Param("teamId") Long teamId);

    boolean existsByIdAndChatroomId(Long id, Long chatroomId);
}
