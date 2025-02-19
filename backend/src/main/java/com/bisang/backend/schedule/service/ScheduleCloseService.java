package com.bisang.backend.schedule.service;

import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.ScheduleStatus.*;
import static com.bisang.backend.schedule.domain.ScheduleStatus.CLOSED;

@Component
@RequiredArgsConstructor
public class ScheduleCloseService {
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;

    @Async("scheduleCloseExecutor")
    public Future<Long> closeAdhocSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedAdhocSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(AD_HOC, lastDateTime);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            Long nextId = closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
            return CompletableFuture.completedFuture(nextId);
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(CLOSED);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return CompletableFuture.completedFuture(null);
    }

    @Async("scheduleCloseExecutor")
    public Future<Long> closeAdhocScheduleById(Long lastScheduleId) {
        List<TeamSchedule> closedAdhocSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndScheduleIdLt(AD_HOC, lastScheduleId);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            Long nextId = closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
            return CompletableFuture.completedFuture(nextId);
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(CLOSED);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return CompletableFuture.completedFuture(null);
    }

    @Async("scheduleCloseExecutor")
    public Future<Long> closeRegularSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedRegularSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(REGULAR, lastDateTime);
        if (closedRegularSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedRegularSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedRegularSchedules);
            Long nextId = closedRegularSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
            return CompletableFuture.completedFuture(nextId);
        }

        for (TeamSchedule teamSchedule : closedRegularSchedules) {
            teamSchedule.updateStatus(CLOSED);
        }
        teamScheduleJpaRepository.saveAll(closedRegularSchedules);
        return CompletableFuture.completedFuture(null);
    }

    @Async("scheduleCloseExecutor")
    public Future<Long> closeRegularScheduleById(Long lastScheduleId) {
        List<TeamSchedule> closedAdhocSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndScheduleIdLt(REGULAR, lastScheduleId);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            Long nextId = closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
            return CompletableFuture.completedFuture(nextId);
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(CLOSED);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return CompletableFuture.completedFuture(null);
    }
}
