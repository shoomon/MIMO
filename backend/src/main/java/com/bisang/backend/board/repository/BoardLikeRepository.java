package com.bisang.backend.board.repository;

import com.bisang.backend.board.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
}
