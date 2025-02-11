package com.bisang.backend.chat.repository.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.chat.domain.ChatMessage;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatroomIdAndIdLessThanOrderByIdDesc(Long chatroomId, Long givenId);

    ChatMessage findTopByChatroomIdOrderByIdDesc(Long chatroomId);
}
