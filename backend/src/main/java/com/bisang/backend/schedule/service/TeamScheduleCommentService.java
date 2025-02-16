package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.schedule.domain.TeamScheduleComment;
import com.bisang.backend.schedule.repository.TeamScheduleCommentJpaRepository;
import com.bisang.backend.team.annotation.TeamMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleCommentService {
    private final TeamScheduleCommentJpaRepository teamScheduleCommentJpaRepository;

    @TeamMember
    @Transactional
    public void createComment(
            Long userId,
            Long teamId,
            Long teamScheduleId,
            Long teamUserId,
            Long parentCommentId,
            String content
    ) {
        if (parentCommentId == null) {
            TeamScheduleComment comment = TeamScheduleComment.builder()
                    .userId(userId)
                    .teamUserId(teamUserId)
                    .teamScheduleId(teamScheduleId)
                    .parentCommentId(null)
                    .content(content).build();
            teamScheduleCommentJpaRepository.save(comment);
            return;
        }

        commentValidation(teamScheduleId, parentCommentId);

        TeamScheduleComment comment = TeamScheduleComment.builder()
                .userId(userId)
                .teamUserId(teamUserId)
                .teamScheduleId(teamScheduleId)
                .parentCommentId(parentCommentId)
                .content(content).build();
        teamScheduleCommentJpaRepository.save(comment);
    }

    @TeamMember
    @Transactional
    public void updateComment(
            Long userId,
            Long teamId,
            Long teamScheduleCommentId,
            String content
    ) {
        TeamScheduleComment comment = findCommentById(teamScheduleCommentId);
        hasComment(comment, userId);
        comment.updateScheduleCommentContent(content);
        teamScheduleCommentJpaRepository.save(comment);
    }

    private TeamScheduleComment findCommentById(Long teamScheduleCommentId) {
        return teamScheduleCommentJpaRepository.findById(teamScheduleCommentId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }

    @TeamMember
    @Transactional
    public void deleteComment(
        Long userId,
        Long teamId,
        Long teamScheduleCommentId
    ) {
        TeamScheduleComment comment = findCommentById(teamScheduleCommentId);
        hasComment(comment, userId);
        teamScheduleCommentJpaRepository.delete(comment);
        teamScheduleCommentJpaRepository.deleteByParentCommentId(teamScheduleCommentId);
        /* 댓글 쇼~~~~
        if (comment.getParentCommentId() == null) {
            Long childCommentCnt = teamScheduleCommentJpaRepository.countByParentCommentId(comment.getId());
            if (childCommentCnt > 0) {
                comment.deleteScheduleCommentContent();
                teamScheduleCommentJpaRepository.save(comment);
                return;
            }
            teamScheduleCommentJpaRepository.delete(comment);
            return;
        }

        TeamScheduleComment parentComment = findCommentById(comment.getParentCommentId());
        if (parentComment.getContent().equals("(삭제된 댓글입니다.)")) {
            Long childCommentCnt
                    = teamScheduleCommentJpaRepository.countByParentCommentId(comment.getParentCommentId());
            if (childCommentCnt == 1) {
                teamScheduleCommentJpaRepository.delete(comment);
                teamScheduleCommentJpaRepository.delete(parentComment);
                return;
            }
            teamScheduleCommentJpaRepository.delete(comment);
            return;
        }
         */
    }

    private void hasComment(
            TeamScheduleComment comment,
            Long userId
    ) {
        if (!comment.getUserId().equals(userId)) {
            throw new ScheduleException(INVALID_REQUEST);
        }
    }

    private void commentValidation(
        Long teamScheduleId,
        Long parentCommentId
    ) {
        teamScheduleCommentJpaRepository.findByTeamScheduleIdAndId(teamScheduleId, parentCommentId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
