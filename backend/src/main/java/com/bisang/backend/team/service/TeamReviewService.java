package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.annotation.TeamMember;
import com.bisang.backend.team.controller.response.TeamReviewResponse;
import com.bisang.backend.team.domain.TeamReview;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamReviewJpaRepository;
import com.bisang.backend.team.repository.TeamReviewQuerydslRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;

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
    @Transactional
    public void remainReview(Long teamId, Long userId, String memo, Long score) {
        TeamUser teamUser = getTeamUser(teamId, userId);
        teamReviewJpaRepository.save(new TeamReview(teamId, teamUser.getId(), memo, score));
    }

    @Transactional
    public void updateReview(Long userId, Long teamReviewId, Long teamId, String memo, Long score) {
        TeamReview teamReview = getTeamReview(teamReviewId);
        TeamUser teamUser = getTeamUser(teamId, userId);
        reviewValidation(teamReview, teamUser);

        teamReview.updateReview(memo, score);
    }

    @Transactional
    public void deleteReview(Long userId, Long teamId, Long teamReviewId) {
        TeamReview teamReview = getTeamReview(teamReviewId);
        TeamUser teamUser = getTeamUser(teamId, userId);
        reviewValidation(teamReview, teamUser);
        teamReviewJpaRepository.delete(teamReview);
    }

    private void reviewValidation(TeamReview teamReview, TeamUser teamUser) {
        if (!teamReview.getTeamUserId().equals(teamUser.getId())) {
            throw new TeamException(NOT_FOUND);
        }
    }

    private TeamReview getTeamReview(Long teamReviewId) {
        return teamReviewJpaRepository.findById(teamReviewId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private TeamUser getTeamUser(Long teamId, Long userId) {
        return teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
