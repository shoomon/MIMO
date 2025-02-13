package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.BoardLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM BoardLike b WHERE b.boardId = :boardId")
    void deleteByBoardId(Long boardId);

    Optional<BoardLike> findByTeamUserIdAndBoardId(Long teamUserId, Long boardId);
}
