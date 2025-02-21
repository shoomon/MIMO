package com.bisang.backend.schedule.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.schedule.controller.request.TeamScheduleCommentCreateRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleCommentDeleteRequest;
import com.bisang.backend.schedule.controller.request.TeamScheduleCommentUpdateRequest;
import com.bisang.backend.schedule.service.TeamScheduleCommentService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-comment")
@RequiredArgsConstructor
public class TeamScheduleCommentController {
    private final TeamScheduleCommentService teamScheduleCommentService;

    @PostMapping
    public ResponseEntity<Void> createTeamScheduleComment(
            @AuthUser User user,
            @Valid @RequestBody TeamScheduleCommentCreateRequest request
    ) {
        teamScheduleCommentService.createComment(
                user.getId(),
                request.teamId(),
                request.teamScheduleId(),
                request.teamUserId(),
                request.parentCommentId(),
                request.content()
        );
        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateTeamScheduleComment(
            @AuthUser User user,
            @Valid @RequestBody TeamScheduleCommentUpdateRequest request
    ) {
        teamScheduleCommentService.updateComment(
                user.getId(),
                request.teamId(),
                request.teamScheduleCommentId(),
                request.content()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeamScheduleComment(
            @AuthUser User user,
            @Valid @RequestBody TeamScheduleCommentDeleteRequest request
    ) {
        teamScheduleCommentService.deleteComment(
                user.getId(),
                request.teamId(),
                request.teamScheduleCommentId()
        );
        return ResponseEntity.ok().build();
    }
}
