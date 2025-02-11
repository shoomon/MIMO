package com.bisang.backend.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.TeamSchedule;

public interface TeamScheduleJpaRepository extends JpaRepository<TeamSchedule, Long> {
}
