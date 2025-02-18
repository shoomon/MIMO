package com.bisang.backend.board.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.user.domain.User;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.board.controller.request.TeamBoardCreateRequest;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.service.TeamBoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("team-board")
public class TeamBoardController {
    private final TeamBoardService teamBoardService;

    @GetMapping
    public ResponseEntity<TeamBoardListResponse> getTeamBoardList(
            @Guest User user,
            @RequestParam(value = "team") Long teamId
    ) {
        return ResponseEntity.ok(teamBoardService.getTeamBoardList(user, teamId));
    }

    @PostMapping
    public ResponseEntity<String> createTeamBoard(
            @AuthUser User user,
            @Valid @RequestBody TeamBoardCreateRequest request
    ) {
        String boardName = teamBoardService.createTeamBoard(
                user.getId(), request.teamId(), request.teamBoardName()
        );
        return ResponseEntity.ok("게시판 \"" + boardName + "\"이 생성되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTeamBoard(
            @AuthUser User user,
            @RequestParam (value = "team") Long teamId,
            @RequestParam(value = "board") Long teamBoardId
    ) {
        teamBoardService.deleteTeamBoard(user.getId(), teamId, teamBoardId);
        return ResponseEntity.ok("게시판이 삭제되었습니다.");
    }
}
