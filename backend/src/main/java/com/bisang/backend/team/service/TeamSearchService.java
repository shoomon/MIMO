package com.bisang.backend.team.service;

import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TagDto;
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
    public TeamTagSearchResponse getTeamsByTag(Long tagId, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamSearchQuerydslRepository.searchTeams(tagId, pageNumber);
        Long teamsCount = teamSearchQuerydslRepository.searchTeamsCount(tagId);
        return new TeamTagSearchResponse(teamsCount.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagResponse getTagBySearchKeyword(String searchKeyword, Integer pageNumber) {
        List<TagDto> tags = teamSearchQuerydslRepository.searchTags(searchKeyword, pageNumber);
        Long numberOfTags = teamSearchQuerydslRepository.searchTagsCount(searchKeyword);
        return new TeamTagResponse(numberOfTags.intValue(), pageNumber, tags.size(), tags);
    }

}
