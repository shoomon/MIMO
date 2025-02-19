package com.bisang.backend.team.service;

import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.SimpleTagDto;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TagDto;
import com.bisang.backend.team.controller.response.SimpleTagResponse;
import com.bisang.backend.team.controller.response.TeamTagResponse;
import com.bisang.backend.team.controller.response.TeamTagSearchResponse;
import com.bisang.backend.team.controller.response.TeamTitleDescSearchResponse;
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
    public TeamTagResponse getTagBySearchKeyword(String searchKeyword, Integer pageNumber) {
        List<SimpleTagDto> tags = teamSearchQuerydslRepository.searchTags(searchKeyword, pageNumber);
        Long numberOfTags = teamSearchQuerydslRepository.searchTagsCount(searchKeyword);
        return new TeamTagResponse(numberOfTags.intValue(), pageNumber, tags.size(), tags);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public SimpleTagResponse getAreaTags() {
        List<SimpleTagDto> areas = teamSearchQuerydslRepository.findAreaTags();
        return new SimpleTagResponse(areas);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public SimpleTagResponse getCategoryTags() {
        List<SimpleTagDto> areas = teamSearchQuerydslRepository.findCategoryTags();
        return new SimpleTagResponse(areas);
    }
}
