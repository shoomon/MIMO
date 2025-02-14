package com.bisang.backend.team.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.QTag.tag;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamReview.teamReview;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bisang.backend.team.controller.dto.SimpleTeamReviewDto;
import com.bisang.backend.team.domain.*;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamQuerydslRepository {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final TeamJpaRepository teamJpaRepository;

    public TeamDto getTeamInfo(Long userId, Long teamId) {
        isTeamExistValidation(teamId);

        SimpleTeamReviewDto simpleTeamReview = getSimpleTeamReview(teamId);
        Long currentMemberCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        List<String> tags = getTags(teamId);

        return Optional.ofNullable(
                queryFactory
                .select(Projections.constructor(TeamDto.class,
                        team.id,
                        team.teamProfileUri,
                        team.name,
                        teamDescription.description,
                        team.recruitStatus,
                        team.privateStatus,
                        team.areaCode,
                        team.maxCapacity,
                        Expressions.numberTemplate(Long.class, "{0}", currentMemberCount),
                        Expressions.constant(simpleTeamReview == null ? 0D : simpleTeamReview.reviewScore()),
                        Expressions.constant(simpleTeamReview == null ? 0L : simpleTeamReview.reviewCount()),
                        Expressions.constant(tags)
                ))
                .from(team).join(teamDescription).on(team.description.id.eq(teamDescription.id))
                .where(team.id.eq(teamId))
                .fetchOne()).orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    public SimpleTeamDto getSimpleTeamInfo(Long userId, Long teamId) {
        isTeamExistValidation(teamId);

        SimpleTeamReviewDto simpleTeamReview = getSimpleTeamReview(teamId);
        Long userCount = getUserCount(teamId);
        List<String> tags = getTags(teamId);

        return queryFactory
            .select(Projections.constructor(SimpleTeamDto.class,
                team.id,
                team.name,
                team.shortDescription,
                team.teamProfileUri,
                Expressions.constant(simpleTeamReview == null ? 0D : simpleTeamReview.reviewScore()),
                Expressions.constant(simpleTeamReview == null ? 0L : simpleTeamReview.reviewCount()),
                team.maxCapacity,
                Expressions.numberTemplate(Long.class, "{0}", userCount),
                Expressions.constant(tags)
            ))
            .from(team)
            .where(team.id.eq(teamId)).fetchOne();
    }

    public List<SimpleTeamDto> getTeamsByAreaCode(Area areaCode, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        List<SimpleTeamDto> teams = queryFactory
            .select(Projections.constructor(SimpleTeamDto.class,
                    team.id,
                    team.name,
                    team.shortDescription,
                    team.teamProfileUri,
                    Expressions.numberTemplate(Double.class, "{0}", 0.0),
                    Expressions.numberTemplate(Long.class, "{0}", 0L),
                    team.maxCapacity,
                    JPAExpressions.select(teamUser.count())
                            .from(teamUser)
                            .where(teamUser.teamId.eq(team.id)),
                    Expressions.constant(emptyList())
            ))
            .from(team)
            .where(team.areaCode.eq(areaCode).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        List<Long> teamIds = teams.stream()
            .map(SimpleTeamDto::teamId)
            .toList();
        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
            .map(teamDto -> {
                List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                SimpleTeamReviewDto simpleTeamReview
                    = teamReviews.getOrDefault(
                        teamDto.teamId(),
                        new SimpleTeamReviewDto(teamDto.teamId(),0D,0L));
                return createSimpleDto(teamDto, tags, simpleTeamReview);
            })
            .sorted(comparing(SimpleTeamDto::teamId).reversed())
            .toList();
    }

    public List<SimpleTeamDto> getTeamsByCategory(TeamCategory category, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        Long userCount = getUserCount(teamId);
        List<SimpleTeamDto> teams = queryFactory
            .select(Projections.constructor(SimpleTeamDto.class,
                    team.id,
                    team.name,
                    team.shortDescription,
                    team.teamProfileUri,
                    Expressions.numberTemplate(Double.class, "{0}", 0.0),
                    Expressions.numberTemplate(Long.class, "{0}", 0L),
                    team.maxCapacity,
                    Expressions.numberTemplate(Long.class, "{0}", userCount),
                    Expressions.constant(emptyList())
            ))
            .from(team)
            .where(team.category.eq(category).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        List<Long> teamIds = teams.stream()
                                    .map(SimpleTeamDto::teamId)
                                    .toList();
        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
            .map(teamDto -> {
                List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                SimpleTeamReviewDto simpleTeamReview
                    = teamReviews.getOrDefault(
                        teamDto.teamId(),
                        new SimpleTeamReviewDto(teamDto.teamId(), 0D,0L));
                return createSimpleDto(teamDto, tags, simpleTeamReview);
            })
            .sorted(comparing(SimpleTeamDto::teamId).reversed())
            .toList();
    }

    private void isTeamExistValidation(Long teamId) {
        teamJpaRepository.findTeamById(teamId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private Long getUserCount(Long teamId) {
        return queryFactory
            .select(teamUser.count())
            .from(teamUser)
            .where(teamUser.teamId.eq(teamId)).fetchOne();
    }

    private List<String> getTags(Long teamId) {
        return queryFactory
            .select(tag.name)
            .from(teamTag)
            .join(tag).on(teamTag.tagId.eq(tag.id))
            .where(teamTag.teamId.eq(teamId))
            .fetch();
    }

    private Map<Long, SimpleTeamReviewDto> getReviewsByTeamIds(List<Long> teamIds) {
        List<SimpleTeamReviewDto> teamReviews = queryFactory
            .select(Projections.constructor(SimpleTeamReviewDto.class,
                teamReview.teamId,
                teamReview.score.avg(),
                teamReview.count()
            ))
            .from(teamReview)
            .where(teamReview.teamId.in(teamIds))
            .groupBy(teamReview.teamId)
            .fetch();

        return teamReviews.stream()
            .collect(toMap(
                SimpleTeamReviewDto::teamId,
                identity()
            ));
    }

    private Map<Long, List<String>> getTagsByTeamIds(List<Long> teamIds) {
        List<Tuple> results = queryFactory
            .select(teamTag.teamId, tag.name)
            .from(teamTag)
            .join(tag).on(teamTag.tagId.eq(tag.id))
            .where(teamTag.teamId.in(teamIds))
            .fetch();

        return results.stream()
            .collect(groupingBy(
                tuple -> tuple.get(teamTag.teamId),
                mapping(tuple -> tuple.get(tag.name), toList())
            ));
    }

    private SimpleTeamDto createSimpleDto(SimpleTeamDto dto, List<String> tags, SimpleTeamReviewDto simpleTeamReview) {
        return new SimpleTeamDto(
            dto.teamId(),
            dto.name(),
            dto.description(),
            dto.teamProfileUri(),
            simpleTeamReview.reviewScore(),
            simpleTeamReview.reviewCount(),
            dto.maxCapacity(),
            dto.currentCapacity(),
            tags
        );
    }

    private SimpleTeamReviewDto getSimpleTeamReview(Long teamId) {
        return queryFactory
            .select(Projections.constructor(SimpleTeamReviewDto.class,
                teamReview.teamId,
                teamReview.score.avg(),
                teamReview.count()
            ))
            .from(teamReview)
            .where(teamReview.teamId.eq(teamId))
            .groupBy(teamReview.teamId)
            .fetchOne();
    }
}
