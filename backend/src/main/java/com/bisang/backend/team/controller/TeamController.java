package com.bisang.backend.team.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.controller.request.CreateTeamRequest;
import com.bisang.backend.team.controller.request.UpdateTeamAreaRequest;
import com.bisang.backend.team.controller.request.UpdateTeamDescriptionRequest;
import com.bisang.backend.team.controller.request.UpdateTeamNameRequest;
import com.bisang.backend.team.controller.request.UpdateTeamPrivateStatusRequest;
import com.bisang.backend.team.controller.request.UpdateTeamProfileUriRequest;
import com.bisang.backend.team.controller.request.UpdateTeamRecruitStatusRequest;
import com.bisang.backend.team.service.TeamService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> createTeam(
        @AuthUser User user,
        @RequestBody CreateTeamRequest req
    ) {
        teamService.createTeam(
            user.getId(),
            req.nickname(),
            req.notificationStatus(),
            req.name(),
            req.description(),
            req.teamRecruitStatus(),
            req.teamPrivateStatus(),
            req.teamProfileUri(),
            req.area(),
            req.category(),
            req.maxCapacity()
        );
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<TeamDto> getTeamInfo(
        @RequestParam Long teamId
    ) {
        TeamDto teamInfo = teamService.getTeamGeneralInfo(teamId);
        return ResponseEntity.ok(teamInfo);
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateTeamName(
        @AuthUser User user,
        @RequestBody UpdateTeamNameRequest req
    ) {
        teamService.updateTeamName(user.getId(), req.teamId(), req.name());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/description")
    public ResponseEntity<Void> updateTeamDescription(
        @AuthUser User user,
        @RequestBody UpdateTeamDescriptionRequest req
    ) {
        teamService.updateTeamDescription(user.getId(), req.teamId(), req.description());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/recruit-status")
    public ResponseEntity<Void> updateTeamRecruitStatus(
        @AuthUser User user,
        @RequestBody UpdateTeamRecruitStatusRequest req
    ) {
        teamService.updateTeamRecruitStatus(user.getId(), req.teamId(), req.status());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/private-status")
    public ResponseEntity<Void> updateTeamPrivateStatus(
        @AuthUser User user,
        @RequestBody UpdateTeamPrivateStatusRequest req
    ) {
        teamService.updateTeamPrivateStatus(user.getId(), req.teamId(), req.status());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile-uri")
    public ResponseEntity<Void> updateTeamProfileUri(
        @AuthUser User user,
        @RequestBody UpdateTeamProfileUriRequest req
    ) {
        teamService.updateTeamProfileUri(user.getId(), req.teamId(), req.profileUri());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/area")
    public ResponseEntity<Void> updateTeamAreaUri(
        @AuthUser User user,
        @RequestBody UpdateTeamAreaRequest req
    ) {
        teamService.updateTeamArea(user.getId(), req.teamId(), req.area());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeam(
        @AuthUser User user,
        @RequestParam Long teamId
    ) {
        teamService.deleteTeam(user.getId(), teamId);
        return ResponseEntity.ok().build();
    }
}
