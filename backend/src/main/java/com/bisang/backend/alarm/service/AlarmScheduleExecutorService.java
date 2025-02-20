package com.bisang.backend.alarm.service;

import com.bisang.backend.alarm.repository.AlarmQuerydslRepository;
import com.bisang.backend.schedule.domain.TeamSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.bisang.backend.common.utils.PageUtils.SMALL_SCHEDULE_PAGE_SIZE;
import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmScheduleExecutorService {
    private final AlarmScheduleService alarmScheduleService;
    private final AlarmQuerydslRepository alarmQuerydslRepository;

    @Scheduled(cron = "0 * * * * *")
    public void sendAlarmScheduleJob() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(MINUTES);
        log.info(time.toString());
        Long lastScheduleId = null;
        do {
            List<TeamSchedule> schedules = alarmQuerydslRepository.getSchedules(time, lastScheduleId);
            for (TeamSchedule schedule : schedules) {
                alarmScheduleService.sendAlarm(schedule);
            }
            if (!schedules.isEmpty()) {
                Boolean hasNext = schedules.size() >= SMALL_SCHEDULE_PAGE_SIZE;
                lastScheduleId = hasNext ? schedules.get(schedules.size() - 1).getId() : null;
            }
        } while (lastScheduleId != null);
    }
}
