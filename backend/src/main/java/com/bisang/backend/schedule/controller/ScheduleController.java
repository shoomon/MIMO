package com.bisang.backend.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.schedule.controller.request.TeamScheduleCreateRequest;
import com.bisang.backend.schedule.controller.response.TeamScheduleCreateResponse;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.service.TeamScheduleService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final TeamScheduleService teamScheduleService;

    @PostMapping
    public ResponseEntity<TeamScheduleCreateResponse> createSchedule(
        @AuthUser User user,
        @RequestBody TeamScheduleCreateRequest request
    ) {
        TeamSchedule teamSchedule = teamScheduleService.createTeamSchedule(
            user.getId(),
            request.teamId(),
            request.teamUserId(),
            request.title(),
            request.description(),
            request.location(),
            request.date(),
            request.maxParticipants());

        return ResponseEntity.ok(new TeamScheduleCreateResponse(teamSchedule.getId()));
    }
}
