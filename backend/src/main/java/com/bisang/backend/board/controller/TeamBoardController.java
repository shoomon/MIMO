package com.bisang.backend.board.controller;

import com.bisang.backend.board.controller.request.TeamBoardCreateRequest;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.service.TeamBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("team-board")
public class TeamBoardController {
    private final TeamBoardService teamBoardService;

    @GetMapping
    public ResponseEntity<TeamBoardListResponse> getTeamBoardList(@RequestParam(value = "team") Long teamId) {
        return ResponseEntity.ok(teamBoardService.getTeamBoardList(teamId));
    }

    @PostMapping
    public ResponseEntity<String> createTeamBoard(
            @Valid @RequestBody TeamBoardCreateRequest request
    ) {
        String boardName = teamBoardService.createTeamBoard(
                request.teamId(), request.teamBoardName()
        );
        return ResponseEntity.ok("게시판 \""+boardName+"\"이 생성되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTeamBoard(@RequestParam(value = "board") Long teamId) {
        teamBoardService.deleteTeamBoard(teamId);
        return ResponseEntity.ok("게시판이 삭제되었습니다.");
    }
}
