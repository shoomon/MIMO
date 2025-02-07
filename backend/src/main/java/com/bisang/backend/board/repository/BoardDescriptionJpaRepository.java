package com.bisang.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.board.domain.BoardDescription;

public interface BoardDescriptionJpaRepository extends JpaRepository<BoardDescription, Long> {
}
