package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {
}
