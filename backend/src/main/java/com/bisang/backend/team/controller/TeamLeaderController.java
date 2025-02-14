package com.bisang.backend.team.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.invite.controller.response.TeamInvitesResponse;
import com.bisang.backend.invite.service.TeamInviteService;
import com.bisang.backend.team.controller.request.DowngradeRoleRequest;
import com.bisang.backend.team.controller.request.InviteApproveRequest;
import com.bisang.backend.team.controller.request.InviteRejectRequest;
import com.bisang.backend.team.controller.request.LeaderDeleteUserRequest;
import com.bisang.backend.team.controller.request.UpgradeRoleRequest;
import com.bisang.backend.team.controller.response.TeamUserResponse;
import com.bisang.backend.team.domain.TeamUserRole;
import com.bisang.backend.team.service.TeamLeaderService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-leader")
public class TeamLeaderController {
    private final TeamLeaderService teamLeaderService;
    private final TeamInviteService teamInviteService;

    @GetMapping("/users")
    public ResponseEntity<TeamUserResponse> getTeamUser(
        @AuthUser User user,
        @RequestParam Long teamId,
        @RequestParam(required = false) TeamUserRole role,
        @RequestParam(required = false) Long teamUserId
    ) {
        var response = teamLeaderService.findTeamUsers(user.getId(), teamId, role, teamUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invite")
    public ResponseEntity<TeamInvitesResponse> getTeamInvites(
        @AuthUser User user,
        @RequestParam Long teamId,
        @RequestParam(required = false) Long lastTeamInviteId
    ) {
        var teamInvites = teamInviteService.getTeamInvites(user.getId(), teamId, lastTeamInviteId);
        return ResponseEntity.ok(teamInvites);
    }

    @PatchMapping("/invite-approve")
    public ResponseEntity<Void> inviteApprove(
        @AuthUser User user,
        @Valid @RequestBody InviteApproveRequest req
    ) {
        teamLeaderService.approveInviteRequest(user.getId(), req.teamId(), req.inviteId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/invite-reject")
    public ResponseEntity<Void> inviteReject(
        @AuthUser User user,
        @Valid @RequestBody InviteRejectRequest req
    ) {
        teamLeaderService.rejectInviteRequest(user.getId(), req.teamId(), req.inviteId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/role-upgrade")
    public ResponseEntity<Void> upgradeRole(
        @AuthUser User user,
        @Valid @RequestBody UpgradeRoleRequest req
    ) {
        teamLeaderService.upgradeRole(user.getId(), req.teamId(), req.teamUserId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/role-downgrade")
    public ResponseEntity<Void> downgradeRole(
        @AuthUser User user,
        @Valid @RequestBody DowngradeRoleRequest req
    ) {
        teamLeaderService.downgradeRole(user.getId(), req.teamId(), req.teamUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
        @AuthUser User user,
        @Valid @RequestBody LeaderDeleteUserRequest req
    ) {
        teamLeaderService.deleteUser(user.getId(), req.teamId(), req.teamUserId());
        return ResponseEntity.ok().build();
    }
}
