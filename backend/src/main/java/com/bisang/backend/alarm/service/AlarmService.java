package com.bisang.backend.alarm.service;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.response.UserAlarmResponse;
import com.bisang.backend.alarm.domain.Alarm;
import com.bisang.backend.alarm.repository.AlarmJpaRepository;
import com.bisang.backend.alarm.repository.AlarmQuerydslRepository;
import com.bisang.backend.common.exception.AlarmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;

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
    public AlarmDto getAlarmById(Long userId, Long alarmId) {
        Alarm alarm = alarmQuerydslRepository.getAlarm(alarmId);
        userHasAlarm(userId, alarm);
        AlarmDto alarmDto = new AlarmDto(
            alarm.getId(),
            alarm.getUserId(),
            alarm.getScheduleId(),
            alarm.getTitle(),
            alarm.getDescription());
        alarmJpaRepository.delete(alarm);
        return alarmDto;
    }

    private static void userHasAlarm(Long userId, Alarm alarm) {
        if (!alarm.getUserId().equals(userId)) {
            throw new AlarmException(INVALID_REQUEST);
        }
    }
}
