package com.bisang.backend.board.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.board.domain.Board;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount+1 WHERE b.id = :postId")
    void increaseViewCount(Long postId);

    @Modifying
    @Query("UPDATE Board b SET b.likes = b.likes+1 WHERE b.id = :postId")
    void increaseLikeCount(Long postId);

    @Modifying
    @Query("UPDATE Board b SET b.likes = b.likes-1 WHERE b.id = :postId")
    void decreaseLikeCount(Long postId);

    @Modifying
    @Query("DELETE FROM Board b WHERE b.teamBoardId = :teamBoardId")
    void deleteAllByTeamBoardId(@Param(value = "teamBoardId") Long teamBoardId);

    @Query("SELECT b.id FROM Board b WHERE b.teamBoardId = :teamBoardId")
    List<Long> findTeamBoardIdByTeamBoardId(@Param(value = "teamBoardId") Long teamBoardId);

    long countBoardsByTeamBoardId(Long teamBoardId);

    List<Board> getBoardByUserId(Long userId);
}
