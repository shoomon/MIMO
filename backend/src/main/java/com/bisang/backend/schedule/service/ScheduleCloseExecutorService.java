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

@Component
@RequiredArgsConstructor
public class ScheduleCloseExecutorService {
    private final TeamScheduleQuerydslRepository teamScheduleQuerydslRepository;
    private final TeamScheduleJpaRepository teamScheduleJpaRepository;

    @Async("scheduleCloseExecutor")
    public LocalDateTime closeAdhocSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedAdhocSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(AD_HOC, lastDateTime);
        if (closedAdhocSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedAdhocSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
            return closedAdhocSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getDate();
        }

        for (TeamSchedule teamSchedule : closedAdhocSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedAdhocSchedules);
        return null;
    }

    @Async("scheduleCloseExecutor")
    public LocalDateTime closeRegularSchedule(LocalDateTime lastDateTime) {
        List<TeamSchedule> closedRegularSchedules
                = teamScheduleQuerydslRepository.getTeamScheduleByStatusAndDateTime(REGULAR, lastDateTime);
        if (closedRegularSchedules.size() > SMALL_SCHEDULE_PAGE_SIZE) {
            for (TeamSchedule teamSchedule : closedRegularSchedules) {
                teamSchedule.updateStatus(CLOSED);
            }
            teamScheduleJpaRepository.saveAll(closedRegularSchedules);
            return closedRegularSchedules.get(SMALL_SCHEDULE_PAGE_SIZE).getDate();
        }

        for (TeamSchedule teamSchedule : closedRegularSchedules) {
            teamSchedule.updateStatus(AD_HOC);
        }
        teamScheduleJpaRepository.saveAll(closedRegularSchedules);
        return null;
    }

    @Scheduled(cron = "0 * * * * *")
    public void closeScheduleJob() {
        LocalDateTime nextRegularDateTime = LocalDateTime.now();
        do {
            nextRegularDateTime = closeRegularSchedule(nextRegularDateTime);
        } while (nextRegularDateTime != null);

        LocalDateTime nextAdhocDateTime = LocalDateTime.now();
        do {
            nextAdhocDateTime = closeRegularSchedule(nextAdhocDateTime);
        } while (nextAdhocDateTime != null);

    }
}
