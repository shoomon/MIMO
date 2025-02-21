package com.bisang.backend.board.repository;

import jakarta.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.bisang.backend.board.domain.BoardImage;

import java.util.List;

public interface BoardImageJpaRepository extends JpaRepository<BoardImage, Long> {

    @Query("SELECT new com.bisang.backend.board.controller.dto.BoardFileDto(b.id, b.fileExtension, b.fileUri) "
            + "FROM BoardImage b WHERE b.boardId = :boardId")
    List<BoardFileDto> findByBoardId(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BoardImage b WHERE b.boardId = :boardId")
    void deleteByBoardId(Long boardId);
}
