package com.bisang.backend.alarm.service;

import com.bisang.backend.alarm.repository.AlarmQuerydslRepository;
import com.bisang.backend.schedule.domain.TeamSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@RequiredArgsConstructor
public class AlarmScheduleExecutorService {
    private final AlarmScheduleService alarmScheduleService;
    private final AlarmQuerydslRepository alarmQuerydslRepository;

    @Scheduled(cron = "0 * * * * *")
    public void sendAlarmScheduleJob() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(MINUTES);
        Long lastScheduleId = null;
        do {
            List<TeamSchedule> schedules = alarmQuerydslRepository.getSchedules(time, lastScheduleId);
            schedules.forEach(alarmScheduleService::sendAlarm);
            lastScheduleId = schedules.isEmpty() ? null : schedules.get(schedules.size() - 1).getId();
        } while (lastScheduleId != null);
    }
}
