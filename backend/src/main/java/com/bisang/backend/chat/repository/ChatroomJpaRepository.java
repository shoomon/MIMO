package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, Long> {
}
