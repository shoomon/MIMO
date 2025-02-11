package com.bisang.backend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.chat.domain.Chatroom;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, Long> {
}
