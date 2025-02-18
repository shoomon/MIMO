package com.bisang.backend.team.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;

import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.repository.TeamJpaRepository;
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
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamReviewJpaRepository teamReviewJpaRepository;
    private final TeamReviewQuerydslRepository teamReviewQuerydslRepository;

    @Transactional(readOnly = true)
    public TeamReviewResponse getTeamReview(
        Long userId, Long teamId, Long lastTeamReviewId
    ) {
        privateTeamValidation(userId, teamId);

        var reviews = teamReviewQuerydslRepository.findTeamReviewByTeamIdAndId(teamId, lastTeamReviewId);
        Boolean hasNext = reviews.size() > PAGE_SIZE;
        Integer size = hasNext ? PAGE_SIZE : reviews.size();
        Long nextLastTeamReviewId = hasNext ? reviews.get(size - 1).teamReviewId() : null;
        if (hasNext) {
            reviews = reviews.stream()
                .limit(size)
                .toList();
        }

        return new TeamReviewResponse(size, hasNext, nextLastTeamReviewId, reviews);
    }

    @TeamMember
    @Transactional
    public void remainReview(Long userId, Long teamId, String memo, Long score) {
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

    private void privateTeamValidation(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        if (team.getPrivateStatus().equals(PRIVATE)) {
            privateGuestUserValidation(userId);
            getTeamUser(teamId, userId);
        }
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findTeamById(teamId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
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

    private void privateGuestUserValidation(Long userId) {
        if (userId == null) {
            throw new TeamException(INVALID_REQUEST);
        }
    }
}
