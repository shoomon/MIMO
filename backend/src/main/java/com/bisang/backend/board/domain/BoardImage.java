package com.bisang.backend.board.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "board_image",
    indexes = {
        @Index(name = "idx_board", columnList = "team_board_id")
    }
)
public class BoardImage {
    @Id @Column(name = "board_image_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(length = 10, name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(length = 100, name = "file_uri", nullable = false)
    private String fileUri;

    private BoardImage(Long boardId, String fileExtension, String fileUri) {
        this.boardId = boardId;
        this.fileExtension = fileExtension;
        this.fileUri = fileUri;
    }
}
