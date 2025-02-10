package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount+1 WHERE b.id = :postId")
    void increaseViewCount(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.likes = b.likes+1 WHERE b.id = :postId")
    void increaseLikeCount(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.likes = b.likes-1 WHERE b.id = :postId")
    void decreaseLikeCount(Long postId);
}
