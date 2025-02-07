package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageJpaRepository extends JpaRepository<BoardImage, Long> {
}
