package com.bisang.backend.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.ScheduleParticipants;

public interface ScheduleParticipantsJpaRepository extends JpaRepository<ScheduleParticipants, Long> {
}
