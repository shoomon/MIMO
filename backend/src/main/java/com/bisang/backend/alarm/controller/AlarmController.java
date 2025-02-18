package com.bisang.backend.alarm.controller;

import com.bisang.backend.alarm.controller.response.UserAlarmResponse;
import com.bisang.backend.alarm.service.AlarmService;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<UserAlarmResponse> getUserAlarms(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(alarmService.getUserAlarmByUserId(user.getId()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserAlarm(
        @AuthUser User user,
        @RequestParam("alarmId") Long alarmId
    ) {

    }
}
