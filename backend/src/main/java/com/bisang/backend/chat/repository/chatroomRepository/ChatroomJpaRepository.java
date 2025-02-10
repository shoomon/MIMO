package com.bisang.backend.chat.repository.chatroomRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.chat.domain.Chatroom;
import org.springframework.data.jpa.repository.Query;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, Long> {

    @Query("SELECT c.title, c.profileUri FROM Chatroom c WHERE c.id = :chatroomId")
    Object[] findTitleAndProfileUriById(@Param("chatroomId") Long chatroomId);

}
