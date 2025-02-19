package com.bisang.backend.alarm.controller.response;

import com.bisang.backend.alarm.controller.dto.AlarmDto;
import com.bisang.backend.alarm.controller.dto.TempAlarmDto;

import java.util.List;

public record UserAlarmResponse(
    List<TempAlarmDto> userAlarms
) {
}
