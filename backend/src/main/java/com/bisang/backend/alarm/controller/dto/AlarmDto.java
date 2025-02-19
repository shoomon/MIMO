package com.bisang.backend.alarm.controller.dto;

public record AlarmDto(
    Long alarmId,
    Long userId,
    Long teamId,
    Long scheduleId,
    String title,
    String description
) {
}
