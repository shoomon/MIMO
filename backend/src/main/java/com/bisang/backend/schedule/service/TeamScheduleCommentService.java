package com.bisang.backend.schedule.service;

import com.bisang.backend.common.exception.ScheduleException;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.schedule.domain.TeamScheduleComment;
import com.bisang.backend.schedule.repository.TeamScheduleCommentJpaRepository;
import com.bisang.backend.team.annotation.TeamMember;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TeamScheduleCommentService {
    private TeamScheduleCommentJpaRepository teamScheduleCommentJpaRepository;
    private TeamUserJpaRepository teamUserJpaRepository;

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
        teamScheduleCommentJpaRepository.findByScheduleIdAndId(teamScheduleId, parentCommentId)
                .orElseThrow(() -> new ScheduleException(NOT_FOUND));
    }
}
