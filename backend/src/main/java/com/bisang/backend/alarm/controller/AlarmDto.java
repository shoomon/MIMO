package com.bisang.backend.alarm.controller;

public record AlarmDto(
    Long alarmId,
    Long userId,
    Long scheduleId,
    String title,
    String description
) {
}
