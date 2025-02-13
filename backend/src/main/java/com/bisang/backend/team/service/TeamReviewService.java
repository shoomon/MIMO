package com.bisang.backend.team.service;

import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.repository.TeamReviewJpaRepository;
import com.bisang.backend.team.repository.TeamReviewQuerydslRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TeamReviewService {
    private final TeamReviewJpaRepository teamReviewJpaRepository;
    private final TeamReviewQuerydslRepository teamReviewQuerydslRepository;

    @Transactional(readOnly = true)
    public TeamReviewResponse getTeamReview(
        Long teamId, Long lastTeamReviewId
    ) {
        var reviews = teamReviewQuerydslRepository.findTeamReviewByTeamIdAndId(teamId, lastTeamReviewId);
        Boolean hasNext = reviews.size() > PAGE_SIZE;
        Integer size = hasNext ? PAGE_SIZE : reviews.size();
        Long nextLastTeamReviewId = hasNext ? reviews.get(size - 1).teamReviewId() : null;
        if (hasNext) {
            reviews.remove(size - 1);
        }

        return new TeamReviewResponse(size, hasNext, nextLastTeamReviewId, reviews);
    }
}
