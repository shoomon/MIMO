package com.bisang.backend.alarm.service;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.response.UserAlarmResponse;
import com.bisang.backend.alarm.domain.Alarm;
import com.bisang.backend.alarm.repository.AlarmJpaRepository;
import com.bisang.backend.alarm.repository.AlarmQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmQuerydslRepository alarmQuerydslRepository;

    @Transactional(readOnly = true)
    public UserAlarmResponse getUserAlarmByUserId(Long userId) {
        List<AlarmDto> userAlarms = alarmQuerydslRepository.getUserAlarms(userId);
        return new UserAlarmResponse(userAlarms);
    }

    @Transactional
    public AlarmDto getAlarmById(Long alarmId) {
        Alarm alarm = alarmQuerydslRepository.getAlarm(alarmId);
        AlarmDto alarmDto = new AlarmDto(
            alarm.getId(),
            alarm.getUserId(),
            alarm.getScheduleId(),
            alarm.getTitle(),
            alarm.getDescription());
        alarmJpaRepository.delete(alarm);
        return alarmDto;
    }
}
