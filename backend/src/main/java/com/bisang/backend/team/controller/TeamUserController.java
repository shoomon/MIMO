package com.bisang.backend.team.controller;

import com.bisang.backend.team.controller.response.TeamUserCreateResponse;
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
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.team.controller.dto.MyTeamUserInfoDto;
import com.bisang.backend.team.controller.request.InviteTeamRequest;
import com.bisang.backend.team.controller.request.JoinTeamRequest;
import com.bisang.backend.team.controller.request.UpdateTeamUserNicknameRequest;
import com.bisang.backend.team.controller.response.SingleTeamUserInfoResponse;
import com.bisang.backend.team.controller.response.TeamInfosResponse;
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

    @GetMapping("/my-info")
    public ResponseEntity<MyTeamUserInfoDto> getMyTeamInfo(
            @Guest User user,
            @RequestParam(name = "teamId") Long teamId
    ) {
        Long userId = user == null ? null : user.getId();
        return ResponseEntity.ok(teamUserService.getMyTeamUserInfo(teamId, userId));
    }

    @GetMapping("/my-team-info")
    public ResponseEntity<TeamInfosResponse> getMyTeams(
            @AuthUser User user,
            @RequestParam(name = "teamId", required = false) Long teamId
    ) {
        return ResponseEntity.ok(teamUserService.getMyTeamInfos(user.getId(), teamId));
    }

    @GetMapping("/exist-nickname")
    public ResponseEntity<Boolean> existsTeamUserNickname(
        @Guest User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "nickname") String nickname
    ) {
        Long userId = user == null ? null : user.getId();
        return ResponseEntity.ok(teamUserService.existsNicknameByTeamIdAndNickname(userId, teamId, nickname));
    }

    @GetMapping("/users")
    public ResponseEntity<TeamUserResponse> getTeamUser(
        @Guest User user,
        @RequestParam Long teamId,
        @RequestParam(required = false) TeamUserRole role,
        @RequestParam(required = false) Long teamUserId
    ) {
        Long userId = user == null ? null : user.getId();
        var response = teamUserService.findTeamUsers(userId, teamId, role, teamUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TeamUserCreateResponse> joinTeam(
        @AuthUser User user,
        @Valid @RequestBody JoinTeamRequest req
    ) {
        Long id = teamUserService.joinTeam(user.getId(), req.teamId(), req.nickname(), req.notificationStatus());
        return ResponseEntity.ok(new TeamUserCreateResponse(id));
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
        @Guest User user,
        @RequestParam Long teamId
    ) {
        Long userId = user == null ? null : user.getId();
        var singleTeamUserInfo = teamUserService.getSingleTeamUserInfo(userId, teamId);
        return ResponseEntity.ok(singleTeamUserInfo);
    }

    @PatchMapping
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        @Valid @RequestBody UpdateTeamUserNicknameRequest req
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
