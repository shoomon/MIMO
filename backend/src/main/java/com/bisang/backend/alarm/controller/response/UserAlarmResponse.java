package com.bisang.backend.alarm.controller.response;

import com.bisang.backend.alarm.controller.dto.AlarmDto;

import java.util.List;

public record UserAlarmResponse(
    List<AlarmDto> userAlarms
) {
}
