package com.bisang.backend.board.domain;


import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "comment",
    indexes = {
        @Index(name = "idx_comment", columnList = "user_id, comment_id desc")
    }
)
public class Comment extends BaseTimeEntity {
    @Id @Column(name = "comment_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    private Comment(
        Long boardId,
        Long teamUserId,
        Long userId,
        Long parentCommentId,
        String content
    ) {
        this.boardId = boardId;
        this.teamUserId = teamUserId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public void updateContent(String content){
        this.content = content;
    }
}
