package com.bisang.backend.team.controller;

import jakarta.validation.Valid;

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
import com.bisang.backend.team.controller.request.InviteTeamRequest;
import com.bisang.backend.team.controller.request.JoinTeamRequest;
import com.bisang.backend.team.controller.request.UpdateTeamUserNicknameRequest;
import com.bisang.backend.team.controller.response.SingleTeamUserInfoResponse;
import com.bisang.backend.team.controller.response.TeamUserResponse;
import com.bisang.backend.team.domain.TeamUserRole;
import com.bisang.backend.team.service.TeamUserService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-user")
public class TeamUserController {
    private final TeamUserService teamUserService;

    @GetMapping("/users")
    public ResponseEntity<TeamUserResponse> getTeamUser(
        @AuthUser User user,
        @RequestParam Long teamId,
        @RequestParam(required = false) TeamUserRole role,
        @RequestParam(required = false) Long teamUserId
    ) {
        var response = teamUserService.findTeamUsers(user.getId(), teamId, role, teamUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> joinTeam(
        @AuthUser User user,
        @Valid @RequestBody JoinTeamRequest req
    ) {
        teamUserService.joinTeam(user.getId(), req.teamId(), req.nickname(), req.notificationStatus());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> inviteTeam(
        @AuthUser User user,
        @Valid @RequestBody InviteTeamRequest req
    ) {
        teamUserService.inviteRequest(user.getId(), req.teamId(), req.memo());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<SingleTeamUserInfoResponse> getSingleTeamUserInfo(
        @AuthUser User user,
        @RequestParam Long teamId
    ) {
        var singleTeamUserInfo = teamUserService.getSingleTeamUserInfo(user.getId(), teamId);
        return ResponseEntity.ok(singleTeamUserInfo);
    }

    @PatchMapping
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        @RequestBody UpdateTeamUserNicknameRequest req
    ) {
        teamUserService.updateNickname(user.getId(), req.teamId(), req.nickname());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamUser(
        @AuthUser User user,
        @RequestParam Long teamId
    ) {
        teamUserService.deleteUser(user.getId(), teamId);
        return ResponseEntity.ok().build();
    }
}
