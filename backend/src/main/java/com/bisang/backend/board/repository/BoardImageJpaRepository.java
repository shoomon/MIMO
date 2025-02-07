package com.bisang.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bisang.backend.board.domain.BoardImage;

public interface BoardImageJpaRepository extends JpaRepository<BoardImage, Long> {
}
