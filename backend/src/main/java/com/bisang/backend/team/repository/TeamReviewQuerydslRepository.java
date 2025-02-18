package com.bisang.backend.team.repository;

import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.team.domain.QTeamReview.teamReview;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bisang.backend.team.controller.dto.TeamReviewDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamReviewQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<TeamReviewDto> findTeamReviewByTeamIdAndId(Long teamId, Long lastTeamReviewId) {
        BooleanBuilder teamReviewIdLt = new BooleanBuilder();
        if (lastTeamReviewId != null) {
            teamReviewIdLt.and(teamReview.id.lt(lastTeamReviewId));
        }

         return queryFactory
            .select(Projections.constructor(TeamReviewDto.class,
                teamReview.id,
                teamReview.memo,
                teamReview.score,
                teamReview.createdAt))
            .from(teamReview)
            .where(teamReview.teamId.eq(teamId), teamReviewIdLt)
            .orderBy(teamReview.id.desc())
            .limit(PAGE_SIZE + 1)
            .fetch();
    }
}
