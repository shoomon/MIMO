package com.bisang.backend.schedule.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleCloseExecutorService {
    private final ScheduleCloseService scheduleCloseService;

    @Scheduled(cron = "*/20 * * * * *")
    public void closeScheduleJob() throws ExecutionException, InterruptedException {
        LocalDateTime nextRegularDateTime = LocalDateTime.now();
        Long lastRegularScheduleId = scheduleCloseService.closeRegularSchedule(nextRegularDateTime).get();
        if (lastRegularScheduleId != null) {
            do {
                lastRegularScheduleId = scheduleCloseService.closeRegularScheduleById(lastRegularScheduleId).get();
            } while (lastRegularScheduleId != null);
        }

        LocalDateTime nextAdhocDateTime = LocalDateTime.now();
        Long lastAdhocScheduleId = scheduleCloseService.closeAdhocSchedule(nextAdhocDateTime).get();
        if (lastAdhocScheduleId != null) {
            do {
                lastAdhocScheduleId = scheduleCloseService.closeAdhocScheduleById(lastAdhocScheduleId).get();
            } while (lastAdhocScheduleId != null);
        }
    }
}
