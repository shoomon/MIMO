package com.bisang.backend.team.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.controller.request.CreateTeamRequest;
import com.bisang.backend.team.controller.request.UpdateTeamRequest;
import com.bisang.backend.team.controller.response.TeamIdResponse;
import com.bisang.backend.team.controller.response.TeamInfosResponse;
import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.service.TeamReviewService;
import com.bisang.backend.team.service.TeamService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;
    private final TeamReviewService teamReviewService;

    @GetMapping("/review")
    public ResponseEntity<TeamReviewResponse> getTeamReviews(
        @Guest User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "lastTeamReviewId", required = false) Long lastTeamReviewId
    ) {
        var reviews = teamReviewService.getTeamReview(teamId, lastTeamReviewId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/exist-name")
    public ResponseEntity<Boolean> existTeamName(
        @Guest User user,
        @RequestParam(name = "name") String name
    ) {
        return ResponseEntity.ok(teamService.existsTeamByName(name));
    }

    @GetMapping("/area")
    public ResponseEntity<TeamInfosResponse> getTeamsByArea(
        @RequestParam("area") Area area,
        @RequestParam(required = false) Long teamId
    ) {
        return ResponseEntity.ok(teamService.getTeamInfosByArea(area, teamId));
    }

    @GetMapping("/category")
    public ResponseEntity<TeamInfosResponse> getTeamsByCategory(
        @RequestParam("category") TeamCategory category,
        @RequestParam(required = false) Long teamId
    ) {
        return ResponseEntity.ok(teamService.getTeamInfosByCategory(category, teamId));
    }

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<TeamIdResponse> createTeam(
        @AuthUser User user,
        @Valid @ModelAttribute CreateTeamRequest req
    ) {
        Long teamId = teamService.createTeam(
            user.getId(),
            req.nickname(),
            req.notificationStatus(),
            req.name(),
            req.description(),
            req.teamRecruitStatus(),
            req.teamPrivateStatus(),
            req.teamProfile(),
            req.area(),
            req.category(),
            req.maxCapacity()
        );
        return ResponseEntity.status(CREATED).body(new TeamIdResponse(teamId));
    }

    @PutMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateTeam(
            @AuthUser User user,
            @Valid @ModelAttribute UpdateTeamRequest req
    ) {
        teamService.updateTeam(
                user.getId(),
                req.teamId(),
                req.name(),
                req.description(),
                req.recruitStatus(),
                req.privateStatus(),
                req.profile(),
                req.area()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<TeamDto> getTeamInfo(
            @Guest User user,
            @RequestParam Long teamId
    ) {
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        TeamDto teamInfo = teamService.getTeamGeneralInfo(userId, teamId);
        return ResponseEntity.ok(teamInfo);
    }

    @GetMapping("/simple")
    public ResponseEntity<SimpleTeamDto> getSimpleTeamInfo(
            @Guest User user,
            @RequestParam Long teamId
    ) {
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        SimpleTeamDto teamInfo = teamService.getSimpleTeamInfo(userId, teamId);
        return ResponseEntity.ok(teamInfo);
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
