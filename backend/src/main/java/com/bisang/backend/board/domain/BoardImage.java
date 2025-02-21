package com.bisang.backend.board.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "board_image",
    indexes = {
        @Index(name = "idx_board", columnList = "board_id")
    }
)
public class BoardImage {
    @Id @Column(name = "board_image_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_board_id", nullable = false)
    private Long teamBoardId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(length = 10, name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(length = 100, name = "file_uri", nullable = false)
    private String fileUri;

    @Builder
    private BoardImage(Long teamBoardId, Long boardId, Long teamId, String fileExtension, String fileUri) {
        this.teamBoardId = teamBoardId;
        this.boardId = boardId;
        this.teamId = teamId;
        this.fileExtension = fileExtension;
        this.fileUri = fileUri;
    }
}
