package com.bisang.backend.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.ScheduleDescription;

public interface ScheduleDescriptionJpaRepository extends JpaRepository<ScheduleDescription, Long> {
}
