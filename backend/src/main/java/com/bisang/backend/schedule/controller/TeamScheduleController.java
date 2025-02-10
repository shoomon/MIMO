package com.bisang.backend.schedule.controller;

import static com.bisang.backend.schedule.domain.ScheduleStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.schedule.controller.request.TeamScheduleCreateRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateDateRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateDescriptionRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateLocationRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateParticipantsRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdatePriceRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleUpdateTitleRequest;
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
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(teamId, AD_HOC, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

    @GetMapping("/regular")
    public ResponseEntity<TeamSchedulesResponse> getRegularSchedules(
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(teamId, REGURAL, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

    @GetMapping("/closed")
    public ResponseEntity<TeamSchedulesResponse> getClosedSchedules(
            @RequestParam Long teamId,
            @RequestParam(required = false) Long lastTeamScheduleId
    ) {
        var adhocSchedule = teamScheduleEveryOneService.getSchedules(teamId, CLOSED, lastTeamScheduleId);
        return ResponseEntity.ok(adhocSchedule);
    }

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

    @PatchMapping("/title")
    public ResponseEntity<Void> updateTitle(
        @AuthUser User user,
        @RequestBody TeamScheduleUpdateTitleRequest request
    ) {
        teamScheduleService.updateTitle(
            user.getId(),
            request.teamId(),
            request.teamScheduleId(),
            request.title()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/description")
    public ResponseEntity<Void> updateDescription(
        @AuthUser User user,
        @RequestBody TeamScheduleUpdateDescriptionRequest request
    ) {
        teamScheduleService.updateDescription(
            user.getId(),
            request.teamId(),
            request.teamScheduleId(),
            request.description()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/date")
    public ResponseEntity<Void> updateDate(
        @AuthUser User user,
        @RequestBody TeamScheduleUpdateDateRequest request
    ) {
        teamScheduleService.updateDate(
            user.getId(),
            request.teamId(),
            request.teamScheduleId(),
            request.date()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/location")
    public ResponseEntity<Void> updateLocation(
        @AuthUser User user,
        @RequestBody TeamScheduleUpdateLocationRequest request
    ) {
        teamScheduleService.updateLocation(
            user.getId(),
            request.teamId(),
            request.teamScheduleId(),
            request.location()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/max-participants")
    public ResponseEntity<Void> updateParticipants(
        @AuthUser User user,
        @RequestBody TeamScheduleUpdateParticipantsRequest request
    ) {
        teamScheduleService.updateParticipants(
            user.getId(),
            request.teamId(),
            request.teamScheduleId(),
            request.maxParticipants()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/price")
    public ResponseEntity<Void> updatePrice(
            @AuthUser User user,
            @RequestBody TeamScheduleUpdatePriceRequest request
    ) {
        teamScheduleService.updatePrice(
                user.getId(),
                request.teamId(),
                request.teamScheduleId(),
                request.price()
        );

        return ResponseEntity.ok().build();
    }
}
