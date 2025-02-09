package com.bisang.backend.schedule.service;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.schedule.domain.TeamScheduleComment;
import com.bisang.backend.schedule.repository.TeamScheduleCommentJpaRepository;
import com.bisang.backend.team.annotation.TeamMember;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void commentValidation(
        Long teamScheduleId,
        Long parentCommentId
    ) {
        teamScheduleCommentJpaRepository.findByScheduleIdAndId(teamScheduleId, parentCommentId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
