package com.bisang.backend.team.controller;

import com.bisang.backend.team.controller.response.*;
import com.bisang.backend.team.service.TeamSearchService;
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
    private final TeamSearchService teamSearchService;

    @GetMapping("/title-description")
    public ResponseEntity<TeamTitleDescSearchResponse> getTitleDescription(
        @RequestParam String searchKeyword,
        @RequestParam Integer pageNumber
    )  {
        return ResponseEntity.ok(teamSearchService.getTeamsByTitleOrDescription(searchKeyword, pageNumber));
    }

    @GetMapping("/tag-team")
    public ResponseEntity<TeamTagSearchResponse> getTagTeam(
        @RequestParam String searchKeyword,
        @RequestParam Integer pageNumber
    ) {
        return ResponseEntity.ok(teamSearchService.getTeamsByTag(searchKeyword, pageNumber));
    }

    @GetMapping("/tag-area")
    public ResponseEntity<TagsResponse> getAreaTag() {
        return ResponseEntity.ok(teamSearchService.getAreaTags());
    }

    @GetMapping("/tag-category")
    public ResponseEntity<TagsResponse> getCategoryTag() {
        return ResponseEntity.ok(teamSearchService.getCategoryTags());
    }
}

