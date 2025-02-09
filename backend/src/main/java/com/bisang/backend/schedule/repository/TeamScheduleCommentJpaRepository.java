package com.bisang.backend.schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.TeamScheduleComment;

public interface TeamScheduleCommentJpaRepository extends JpaRepository<TeamScheduleComment, Long> {
    Optional<TeamScheduleComment> findByScheduleIdAndId(Long scheduleId, Long id);
}
