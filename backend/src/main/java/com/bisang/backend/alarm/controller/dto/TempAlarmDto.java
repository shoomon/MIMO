package com.bisang.backend.alarm.controller.dto;

public record TempAlarmDto(
    Long userId,
    Long scheduleId,
    String title,
    String description
) {
}
