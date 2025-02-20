package com.bisang.backend.team.domain;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bisang.backend.common.exception.TeamException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team_review",
        indexes = {
            @Index(name = "idx_team_review_id", columnList = "team_id, team_review_id desc")
        }
)
public class TeamReview {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_review_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_user_id", nullable = false, unique = true)
    private Long teamUserId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "review_memo", length = 300, nullable = false)
    private String memo;

    @Column(name = "review_score", nullable = false)
    private Long score;

    @Builder
    public TeamReview(Long teamId, Long teamUserId, String memo, Long score) {
        this.teamId = teamId;
        this.teamUserId = teamUserId;
        this.memo = memo;
        memoValidation(memo);
        this.score = score;
        scoreValidation(score);
    }

    private void memoValidation(String memo) {
        String pattern = "^.{0,300}$";
        Validate.matchesPattern(memo, pattern,
                "모임 리뷰의 길이는 300자리 이하이어야 합니다.");
    }

    private void scoreValidation(Long score) {
        if (!(score == 1 || score == 2 || score == 3 || score == 4 || score == 5)) {
            throw new TeamException(INVALID_REQUEST);
        }
    }

    public void updateReview(String memo, Long score) {
        memoValidation(memo);
        this.memo = memo;
        scoreValidation(score);
        this.score = score;
    }
}
