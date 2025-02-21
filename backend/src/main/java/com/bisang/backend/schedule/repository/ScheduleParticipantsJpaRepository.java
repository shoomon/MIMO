package com.bisang.backend.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.schedule.domain.ScheduleParticipants;

public interface ScheduleParticipantsJpaRepository extends JpaRepository<ScheduleParticipants, Long> {
    Long countByTeamScheduleId(Long scheduleId);

    List<ScheduleParticipants> findByTeamScheduleId(Long teamScheduleId);

    Boolean existsByTeamScheduleIdAndTeamUserId(Long teamScheduleId, Long teamUserId);

    Boolean existsByTeamScheduleIdAndUserId(Long teamScheduleId, Long userId);
}
