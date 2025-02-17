package com.bisang.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.board.domain.TeamBoard;

public interface TeamBoardJpaRepository extends JpaRepository<TeamBoard, Long> {
}
