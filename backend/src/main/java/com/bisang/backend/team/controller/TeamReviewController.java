package com.bisang.backend.team.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.service.TeamReviewService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-review")
public class TeamReviewController {
    private final TeamReviewService teamReviewService;

    @GetMapping
    public ResponseEntity<TeamReviewResponse> getTeamReviews(
        @Guest User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "lastTeamReviewId", required = false) Long lastTeamReviewId
    ) {
        var reviews = teamReviewService.getTeamReview(teamId, lastTeamReviewId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Void> remainTeamReview(
        @AuthUser User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "memo") String memo,
        @RequestParam(name = "score") Long score
    ) {
        teamReviewService.remainReview(teamId, user.getId(), memo, score);
        return ResponseEntity.status(CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateTeamReview(
        @AuthUser User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "teamReviewId") Long teamReviewId,
        @RequestParam(name = "memo") String memo,
        @RequestParam(name = "score") Long score
    ) {
        teamReviewService.updateReview(user.getId(), teamReviewId, teamId, memo, score);
        return ResponseEntity.ok().build();
    }
}
