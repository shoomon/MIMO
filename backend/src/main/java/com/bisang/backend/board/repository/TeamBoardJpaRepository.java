package com.bisang.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.board.domain.TeamBoard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamBoardJpaRepository extends JpaRepository<TeamBoard, Long> {
    @Query("SELECT b.boardName FROM TeamBoard b WHERE b.id = :teamBoardId")
    String getTeamBoardNameById(@Param("teamBoardId") Long teamBoardId);
}
