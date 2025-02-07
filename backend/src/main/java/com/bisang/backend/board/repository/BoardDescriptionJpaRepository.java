package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.BoardDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardDescriptionJpaRepository extends JpaRepository<BoardDescription, Long> {
}
