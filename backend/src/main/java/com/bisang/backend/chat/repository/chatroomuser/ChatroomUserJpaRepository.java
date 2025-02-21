package com.bisang.backend.chat.repository.chatroomuser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.chat.domain.ChatroomUser;

import io.lettuce.core.dynamic.annotation.Param;

public interface ChatroomUserJpaRepository extends JpaRepository<ChatroomUser, Long> {
    @Query("SELECT u.userId FROM ChatroomUser u WHERE u.chatroomId = :teamId")
    Set<Long> findUserIdsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT u.nickname FROM ChatroomUser u WHERE u.chatroomId = :chatroomId AND u.userId = :userId")
    String findNicknameByChatroomIdAndUserId(@Param("chatroomId") Long chatroomId, @Param("userId") Long userId);

    boolean existsByUserIdAndChatroomId(Long userId, Long teamId);

    Optional<ChatroomUser> findByChatroomIdAndUserId(Long chatroomId, Long userId);

    void deleteByChatroomIdAndUserId(Long teamId, Long userId);

    @Query("SELECT c.chatroomId FROM ChatroomUser c WHERE c.userId = :userId")
    List<Long> findAllChatroomIdsByUserId(long userId);
}
