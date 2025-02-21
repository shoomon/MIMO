package com.bisang.backend.schedule.domain;

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
        name = "team_schedule_comment",
        indexes = {
            @Index(name = "idx_team_schedule_comment_schedule_id",
                    columnList = "team_schedule_id, team_schedule_comment_id"),
        }
)
public class TeamScheduleComment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_schedule_comment_id")
    private Long id;

    @Column(name = "team_schedule_id", nullable = false)
    private Long teamScheduleId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Builder
    private TeamScheduleComment(
            Long teamScheduleId,
            Long teamUserId,
            Long userId,
            Long parentCommentId,
            String content
    ) {
        this.teamScheduleId = teamScheduleId;
        this.teamUserId = teamUserId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public void updateScheduleCommentContent(String content) {
        this.content = content;
    }

    public void deleteScheduleCommentContent() {
        this.content = "(삭제된 댓글입니다.)";
    }
}
