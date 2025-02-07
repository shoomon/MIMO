package com.bisang.backend.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.board.domain.Comment;

public interface CommentJpaReporitory extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
}
