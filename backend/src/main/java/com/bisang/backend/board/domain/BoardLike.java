package com.bisang.backend.board.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "board_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "board_id", "team_user_id" })
    }
)
public class BoardLike {
    @Id @Column(name = "board_like_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Builder
    private BoardLike(Long boardId, Long teamUserId) {
        this.boardId = boardId;
        this.teamUserId = teamUserId;
    }
}
