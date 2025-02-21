package com.bisang.backend.team.service;

import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.response.*;
import com.bisang.backend.team.repository.TeamSearchQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamSearchService {
    private final TeamSearchQuerydslRepository teamSearchQuerydslRepository;

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTitleDescSearchResponse getTeamsByTitleOrDescription(String searchKeyword, Integer pageNumber) {
        String fulltextSearchKeyword = createFulltextPattern(searchKeyword);
        List<SimpleTeamDto> teams = teamSearchQuerydslRepository.searchTeams(fulltextSearchKeyword, pageNumber);
        Long numberOfTeams = teamSearchQuerydslRepository.searchTeamsCount(fulltextSearchKeyword);
        return new TeamTitleDescSearchResponse(numberOfTeams.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagSearchResponse getTeamsByTag(String searchKeyword, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamSearchQuerydslRepository.searchTagTeams(searchKeyword, pageNumber);
        Long teamsCount = teamSearchQuerydslRepository.searchTagTeamsCount(searchKeyword);
        return new TeamTagSearchResponse(teamsCount.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TagsResponse getAreaTags() {
        return teamSearchQuerydslRepository.findAreaTags();
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TagsResponse getCategoryTags() {
        return teamSearchQuerydslRepository.findCategoryTags();
    }

    private String createFulltextPattern(String searchKeyword) {
        List<String> searchKeywords = Arrays.stream(searchKeyword.split(" "))
            .map(keyword -> keyword + "*")
            .collect(Collectors.toList());

        return String.join(" ", searchKeywords);
    }
}
