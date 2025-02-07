package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaReporitory extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
}
