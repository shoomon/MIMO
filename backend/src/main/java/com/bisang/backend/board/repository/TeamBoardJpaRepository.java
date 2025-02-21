package com.bisang.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisang.backend.board.domain.TeamBoard;

import java.util.List;
import java.util.Optional;

public interface TeamBoardJpaRepository extends JpaRepository<TeamBoard, Long> {
    @Query("SELECT b.boardName FROM TeamBoard b WHERE b.id = :teamBoardId")
    String getTeamBoardNameById(@Param("teamBoardId") Long teamBoardId);

    @Query("SELECT b FROM TeamBoard b WHERE b.teamId = :teamBoardId ORDER BY b.id asc")
    List<TeamBoard> findAllByTeamId(@Param("teamBoardId") Long teamId);

    @Query("SELECT b.id FROM TeamBoard b WHERE b.teamId = :teamBoardId")
    List<Long> getTeamBoardIdByTeamId(@Param("teamBoardId") Long teamId);

    Optional<TeamBoard> findTeamBoardById(Long id);
}
