package com.bisang.backend.team.service;

import com.bisang.backend.team.annotation.EveryOne;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TagDto;
import com.bisang.backend.team.controller.response.TeamTagResponse;
import com.bisang.backend.team.controller.response.TeamTagSearchResponse;
import com.bisang.backend.team.controller.response.TeamTitleDescSearchResponse;
import com.bisang.backend.team.repository.TeamQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamSearchService {
    private final TeamQuerydslRepository teamQuerydslRepository;

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTitleDescSearchResponse getTeamsByTitleOrDescription(String searchKeyword, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.searchTeams(searchKeyword, pageNumber);
        Long numberOfTeams = teamQuerydslRepository.searchTeamsCount(searchKeyword);
        return new TeamTitleDescSearchResponse(numberOfTeams.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagSearchResponse getTeamsByTag(Long tagId, Integer pageNumber) {
        List<SimpleTeamDto> teams = teamQuerydslRepository.searchTeams(tagId, pageNumber);
        Long teamsCount = teamQuerydslRepository.searchTeamsCount(tagId);
        return new TeamTagSearchResponse(teamsCount.intValue(), pageNumber, teams.size(), teams);
    }

    @EveryOne
    @Transactional(readOnly = true)
    public TeamTagResponse getTagBySearchKeyword(String searchKeyword, Integer pageNumber) {
        List<TagDto> tags = teamQuerydslRepository.searchTags(searchKeyword, pageNumber);
        Long numberOfTags = teamQuerydslRepository.searchTagsCount(searchKeyword);
        return new TeamTagResponse(numberOfTags.intValue(), pageNumber, tags.size(), tags);
    }

}
