package com.bisang.backend.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.TeamScheduleComment;

public interface TeamScheduleCommentRepository extends JpaRepository<TeamScheduleComment, Long> {
}
