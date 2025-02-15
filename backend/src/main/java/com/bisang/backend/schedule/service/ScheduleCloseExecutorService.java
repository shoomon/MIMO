package com.bisang.backend.schedule.service;

import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static com.bisang.backend.schedule.domain.ScheduleStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleQuerydslRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduleCloseExecutorService {
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;

    @Async("scheduleCloseExecutor")
    @Transactional
    public Long closeAdhocSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedAdhocSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(AD_HOC, lastDateTime);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            return closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return null;
    }

    @Async("scheduleCloseExecutor")
    @Transactional
    public Long closeAdhocScheduleById(Long lastScheduleId) {
        List<TeamSchedule> closedAdhocSchedules
            = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndScheduleIdLt(AD_HOC, lastScheduleId);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            return closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return null;
    }

    @Async("scheduleCloseExecutor")
    @Transactional
    public Long closeRegularSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedRegularSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(REGULAR, lastDateTime);
        if (closedRegularSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedRegularSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedRegularSchedules);
            return closedRegularSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
        }

        for (TeamSchedule teamSchedule : closedRegularSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedRegularSchedules);
        return null;
    }

    @Async("scheduleCloseExecutor")
    @Transactional
    public Long closeRegularScheduleById(Long lastScheduleId) {
        List<TeamSchedule> closedAdhocSchedules
            = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndScheduleIdLt(REGULAR, lastScheduleId);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            return closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getId();
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return null;
    }

    @Scheduled(cron = "0 * * * * *")
    public void closeScheduleJob() {
        LocalDateTime nextRegularDateTime = LocalDateTime.now();
        Long lastRegularScheduleId = closeRegularSchedule(nextRegularDateTime);
        if (lastRegularScheduleId != null) {
            do {
                lastRegularScheduleId = closeRegularScheduleById(lastRegularScheduleId);
            } while (lastRegularScheduleId != null);
        }

        LocalDateTime nextAdhocDateTime = LocalDateTime.now();
        Long lastAdhocScheduleId = closeAdhocSchedule(nextAdhocDateTime);
        if (lastAdhocScheduleId != null) {
            do {
                lastAdhocScheduleId = closeAdhocScheduleById(lastAdhocScheduleId);
            } while (lastAdhocScheduleId != null);
        }
    }
}
