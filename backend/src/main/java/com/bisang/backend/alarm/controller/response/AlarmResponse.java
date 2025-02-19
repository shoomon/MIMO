package com.bisang.backend.alarm.controller.response;

import com.bisang.backend.alarm.controller.dto.AlarmDto;

public record AlarmResponse(
    AlarmDto alarm
) {
}
