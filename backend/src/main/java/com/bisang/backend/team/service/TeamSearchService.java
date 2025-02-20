package com.bisang.backend.team.service;

import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.SimpleTagDto;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TagDto;
import com.bisang.backend.team.controller.response.*;
import com.bisang.backend.team.repository.TeamSearchQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamSearchService {
    private final TeamSearchQuerydslRepository teamSearchQuerydslRepository;

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTitleDescSearchResponse getTeamsByTitleOrDescription(String searchKeyword, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamSearchQuerydslRepository.searchTeams(searchKeyword, pageNumber);
        Long numberOfTeams = teamSearchQuerydslRepository.searchTeamsCount(searchKeyword);
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
}
