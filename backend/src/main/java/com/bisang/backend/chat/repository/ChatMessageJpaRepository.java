package com.bisang.backend.chat.repository;

import com.bisang.backend.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatroomIdAndIdLessThanOrderByIdDesc(Long chatroomId, Long givenId);
}
