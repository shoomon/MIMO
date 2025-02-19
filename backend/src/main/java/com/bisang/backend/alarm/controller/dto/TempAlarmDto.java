package com.bisang.backend.alarm.controller.dto;


public record TempAlarmDto(
        Long alarmId,
        Long userId,
        Long scheduleId,
        String title,
        String description
) {
    public String toString() {
        return "userId: " + userId + " scheduleId: " + scheduleId + " title: " + title + " description: " + description;
    }
}
