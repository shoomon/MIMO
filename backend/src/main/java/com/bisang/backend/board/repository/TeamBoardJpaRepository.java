package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.TeamBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBoardJpaRepository extends JpaRepository<TeamBoard, Long> {
}
