package com.bisang.backend.chat.repository.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.chat.domain.ChatMessage;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m "
            + "WHERE m.chatroomId = :chatroomId "
            + "  AND m.id < :givenId "
            + "  AND m.id >= :enterChatId "
            + "ORDER BY m.id DESC")
    List<ChatMessage> getMessages(Long chatroomId, Long givenId, Long enterChatId);

    ChatMessage findTopByChatroomIdOrderByIdDesc(Long chatroomId);

    long countByChatroomIdAndIdGreaterThan(Long chatroomId, Long messageId);
}
