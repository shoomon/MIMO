package com.bisang.backend.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.TeamScheduleComment;

public interface TeamScheduleCommentJpaRepository extends JpaRepository<TeamScheduleComment, Long> {
    Optional<TeamScheduleComment> findByTeamScheduleIdAndId(Long scheduleId, Long id);

    List<TeamScheduleComment> findByTeamScheduleId(Long teamScheduleId);

    Long countById(Long id);

    Long countByParentCommentId(Long parentCommentId);

    void deleteByParentCommentId(Long parentCommentId);
}
