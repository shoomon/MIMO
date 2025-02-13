package com.bisang.backend.schedule.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.schedule.service.ScheduleParticipantsService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-participants")
@RequiredArgsConstructor
public class ScheduleParticipantsController {
    private final ScheduleParticipantsService scheduleParticipantsService;

    @PostMapping
    public ResponseEntity<Void> joinSchedule(
            @AuthUser User user,
            @RequestParam(name = "teamScheduleId") Long teamScheduleId
    ) {
        scheduleParticipantsService.joinSchedule(teamScheduleId.toString(), user.getId(), teamScheduleId);
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> leaveSchedule(
            @AuthUser User user,
            @RequestParam(name = "teamScheduleId") Long teamScheduleId
    ) {
        scheduleParticipantsService.leaveSchedule(teamScheduleId.toString(), user.getId(), teamScheduleId);
        return ResponseEntity.ok().build();
    }
}
