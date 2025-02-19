package com.bisang.backend.alarm.service;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.dto.TempAlarmDto;
import com.bisang.backend.alarm.domain.Alarm;
import com.bisang.backend.alarm.repository.AlarmJpaRepository;
import com.bisang.backend.alarm.repository.AlarmQuerydslRepository;
import com.bisang.backend.schedule.domain.TeamSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmScheduleService {
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmQuerydslRepository alarmQuerydslRepository;

    public void sendAlarm(TeamSchedule teamSchedule) {
        List<TempAlarmDto> alarms = alarmQuerydslRepository.getAlarms(teamSchedule);
        for (TempAlarmDto alarm : alarms) {
            try {
                alarmJpaRepository.save(new Alarm(alarm.userId(), alarm.scheduleId(), alarm.title(), alarm.description()));
            } catch (Exception e) {}
        }
    }
}
