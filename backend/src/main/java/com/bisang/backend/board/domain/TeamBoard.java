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
        name = "team_board",
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_team_board", columnNames = {"team_id", "board_name"})
        }
)
public class TeamBoard {
    @Id @Column(name = "team_board_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(length = 40, name = "board_name", nullable = false)
    private String boardName;

    @Builder
    private TeamBoard(Long teamId, String boardName) {
        this.teamId = teamId;
        this.boardName = boardName;
    }
}
