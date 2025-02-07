package com.bisang.backend.schedule.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.schedule.domain.ScheduleDescription;
import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleDescriptionJpaRepository;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.team.annotation.TeamLeader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamScheduleService {
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;
    private final ScheduleDescriptionJpaRepository scheduleDescriptionJpaRepository;
    private final ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @TeamLeader
    @Transactional
    public TeamSchedule createTeamSchedule(
        Long userId,
        Long teamId,
        Long teamUserId,
        String title,
        String description,
        String location,
        LocalDateTime date,
        Long maxParticipants
    ) {
        ScheduleDescription scheduleDescription = new ScheduleDescription(description);
        scheduleDescriptionJpaRepository.save(scheduleDescription);

        TeamSchedule teamSchedule = TeamSchedule.builder()
            .teamId(teamId)
            .teamUserId(teamUserId)
            .title(title)
            .description(scheduleDescription)
            .location(location)
            .date(date)
            .maxParticipants(maxParticipants).build();
        teamScheduleJpaRepository.save(teamSchedule);


        ScheduleParticipants creator = ScheduleParticipants.creator(teamSchedule.getId(), teamUserId);
        scheduleParticipantsJpaRepository.save(creator);

        return teamSchedule;
    }
}
