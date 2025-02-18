package com.bisang.backend.schedule.controller;

import static com.bisang.backend.schedule.domain.ScheduleStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.schedule.controller.request.TeamScheduleCreateRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateRequest;
import com.bisang.backend.schedule.controller.response.TeamScheduleCreateResponse;
import com.bisang.backend.schedule.controller.response.TeamScheduleSpecificResponse;
import com.bisang.backend.schedule.controller.response.TeamSchedulesResponse;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.service.TeamScheduleEveryOneService;
import com.bisang.backend.schedule.service.TeamScheduleLeaderService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class TeamScheduleController {
    private final TeamScheduleLeaderService teamScheduleService;
    private final TeamScheduleEveryOneService teamScheduleEveryOneService;

    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @AuthUser User user,
            @RequestParam Long teamId,
            @RequestParam Long teamScheduleId
    ) {
        teamScheduleService.deleteTeamSchedule(user.getId(), teamId, teamScheduleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<TeamScheduleSpecificResponse> getSpecificSchedule(
            @Guest User user,
            @RequestParam Long teamId,
            @RequestParam Long teamScheduleId
    ) {
        Long userId = user == null ? null : user.getId();
        var specificSchedule = teamScheduleEveryOneService.getSpecificSchedule(userId, teamId, teamScheduleId);
        return ResponseEntity.ok(specificSchedule);
    }

    @GetMapping("/ad-hoc")
    public ResponseEntity<TeamSchedulesResponse> getAdhocSchedules(
            @Guest User user,
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        Long userId = user == null ? null : user.getId();
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(userId, teamId, AD_HOC, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

    @GetMapping("/regular")
    public ResponseEntity<TeamSchedulesResponse> getRegularSchedules(
            @Guest User user,
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        Long userId = user == null ? null : user.getId();
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(userId, teamId, REGULAR, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

    @GetMapping("/closed")
    public ResponseEntity<TeamSchedulesResponse> getClosedSchedules(
            @Guest User user,
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        Long userId = user == null ? null : user.getId();
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(userId, teamId, CLOSED, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

    @PostMapping
    public ResponseEntity<TeamScheduleCreateResponse> createSchedule(
        @AuthUser User user,
        @Valid @RequestBody TeamScheduleCreateRequest request
    ) {
        TeamSchedule teamSchedule = teamScheduleService.createTeamSchedule(
                user.getId(),
                request.teamId(),
                request.title(),
                request.description(),
                request.location(),
                request.date(),
                request.maxParticipants(),
                request.price(),
                request.status());

        return ResponseEntity.status(CREATED).body(new TeamScheduleCreateResponse(teamSchedule.getId()));
    }

    @PutMapping
    public ResponseEntity<Void> updateTeamSchedule(
            @AuthUser User user,
            @Valid @RequestBody TeamScheduleUpdateRequest request
    ) {
        teamScheduleService.updateTeamSchedule(
                user.getId(),
                request.teamId(),
                request.teamScheduleId(),
                request.title(),
                request.description(),
                request.location(),
                request.date(),
                request.maxParticipants(),
                request.price(),
                request.status());

        return ResponseEntity.ok().build();
    }
}
