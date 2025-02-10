package com.bisang.backend.board.repository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.board.domain.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaReporitory extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.boardId = :boardId")
    void deleteByBoardId(Long boardId);
}
