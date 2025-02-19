package com.bisang.backend.schedule.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleCloseExecutorService {
    private final ScheduleCloseService scheduleCloseService;

    @Scheduled(cron = "*/20 * * * * *")
    public void closeScheduleJob() {
        LocalDateTime nextRegularDateTime = LocalDateTime.now();
        Long lastRegularScheduleId = scheduleCloseService.closeRegularSchedule(nextRegularDateTime);
        if (lastRegularScheduleId != null) {
            do {
                lastRegularScheduleId = scheduleCloseService.closeRegularScheduleById(lastRegularScheduleId);
            } while (lastRegularScheduleId != null);
        }

        LocalDateTime nextAdhocDateTime = LocalDateTime.now();
        Long lastAdhocScheduleId = scheduleCloseService.closeAdhocSchedule(nextAdhocDateTime);
        if (lastAdhocScheduleId != null) {
            do {
                lastAdhocScheduleId = scheduleCloseService.closeAdhocScheduleById(lastAdhocScheduleId);
            } while (lastAdhocScheduleId != null);
        }
    }
}
