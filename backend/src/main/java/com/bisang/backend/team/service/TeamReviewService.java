package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.annotation.TeamMember;
import com.bisang.backend.team.domain.TeamReview;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.repository.TeamReviewJpaRepository;
import com.bisang.backend.team.repository.TeamReviewQuerydslRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TeamReviewService {
    private final TeamUserJpaRepository teamUserJpaRepository;
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

    @TeamMember
    public void remainReview(Long teamId, Long userId, String memo, Long score) {
        TeamUser teamUser = getTeamUser(teamId, userId);
        teamReviewJpaRepository.save(new TeamReview(teamId, teamUser.getId(), memo, score));
    }

    private TeamUser getTeamUser(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
