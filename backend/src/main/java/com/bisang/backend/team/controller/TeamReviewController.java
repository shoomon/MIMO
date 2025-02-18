package com.bisang.backend.team.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.service.TeamReviewService;
import com.bisang.backend.user.domain.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        var reviews = teamReviewService.getTeamReview(userId, teamId, lastTeamReviewId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Void> remainTeamReview(
        @AuthUser User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "memo") String memo,
        @RequestParam(name = "score") Long score
    ) {
        teamReviewService.remainReview(user.getId(), teamId, memo, score);
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

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamReview(
        @AuthUser User user,
        @RequestParam(name = "teamId") Long teamId,
        @RequestParam(name = "teamReviewId") Long teamReviewId
    ) {
        teamReviewService.deleteReview(user.getId(), teamId, teamReviewId);
        return ResponseEntity.ok().build();
    }
}
