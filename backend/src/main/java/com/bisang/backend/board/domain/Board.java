package com.bisang.backend.board.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "board",
        indexes = {
            @Index(name = "idx_board_user", columnList = "user_id"),
            @Index(name = "idx_board", columnList = "team_board_id, board_id desc")
        }
)
public class Board extends BaseTimeEntity {
    @Id @Column(name = "board_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_board_id", nullable = false)
    private Long teamBoardId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 50, name = "board_title", nullable = false)
    private String title;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "board_description_id", referencedColumnName = "board_description_id")
    private BoardDescription description;

    @Column(name = "board_like", nullable = false)
    private Long likes;

    @Column(name = "board_view_count", nullable = false)
    private Long viewCount;

    @Builder
    private Board(
            Long teamBoardId,
            Long teamId,
            Long teamUserId,
            Long userId,
            String title,
            BoardDescription description
    ) {
        this.teamBoardId = teamBoardId;
        this.teamId = teamId;
        this.teamUserId = teamUserId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        likes = 0L;
        viewCount = 0L;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }
}
