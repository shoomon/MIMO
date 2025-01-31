package com.bisang.backend.team_board.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    private TeamBoard(Long teamId, String boardName) {
        this.teamId = teamId;
        this.boardName = boardName;
    }
}
