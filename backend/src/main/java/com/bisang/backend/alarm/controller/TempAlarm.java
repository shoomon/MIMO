package com.bisang.backend.alarm.controller;

public record TempAlarm(
    Long userId,
    Long scheduleId,
    String title,
    String description
) {
}
