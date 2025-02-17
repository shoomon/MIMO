package com.bisang.backend.team.controller;

import com.bisang.backend.team.controller.response.TeamTagResponse;
import com.bisang.backend.team.controller.response.TeamTagSearchResponse;
import com.bisang.backend.team.controller.response.TeamTitleDescSearchResponse;
import com.bisang.backend.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search-team")
public class TeamSearchController {
    private final TeamService teamService;

    @GetMapping("/title-description")
    public ResponseEntity<TeamTitleDescSearchResponse> getTitleDescription(
        @RequestParam String searchKeyword,
        @RequestParam Integer pageNumber
    )  {
        return ResponseEntity.ok(teamService.getTeamsByTitleOrDescription(searchKeyword, pageNumber));
    }

    @GetMapping("/tags")
    public ResponseEntity<TeamTagResponse> getTags(
        @RequestParam String searchKeyword,
        @RequestParam Integer pageNumber
    ) {
        return ResponseEntity.ok(teamService.getTagBySearchKeyword(searchKeyword, pageNumber));
    }

    @GetMapping("/tag-team")
    public ResponseEntity<TeamTagSearchResponse> getTagTeam(
        @RequestParam Long tagId,
        @RequestParam Integer pageNumber
    ) {
        return ResponseEntity.ok(teamService.getTeamsByTag(tagId, pageNumber));
    }
}
