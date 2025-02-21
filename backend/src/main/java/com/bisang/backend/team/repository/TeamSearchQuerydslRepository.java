package com.bisang.backend.team.repository;

import com.bisang.backend.team.controller.dto.*;
import com.bisang.backend.team.controller.response.TagsResponse;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamReview.teamReview;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PUBLIC;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class TeamSearchQuerydslRepository {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamTagJpaRepository teamTagJpaRepository;

    public TagsResponse findAreaTags() {
        List<String> areaNames = Area.getAreaNames();
        return new TagsResponse(areaNames);
    }

    public TagsResponse findCategoryTags() {
        List<String> categoryNames = TeamCategory.getCategoryNames();
        return new TagsResponse(categoryNames);
    }

    public Long searchTagTeamsCount(String searchText) {
        return queryFactory
            .select(teamTag.teamId.count())
            .from(teamTag).join(team).on(teamTag.teamId.eq(team.id))
            .where(team.privateStatus.eq(PUBLIC), teamTag.tagName.eq(searchText))
            .fetchOne();
    }

    public List<SimpleTeamDto> searchTagTeams(String searchText, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }

        List<Long> teamIds = queryFactory
            .select(teamTag.teamId)
            .from(teamTag)
            .where(teamTag.tagName.eq(searchText))
            .orderBy(teamTag.teamId.desc())
            .limit(SHORT_PAGE_SIZE)
            .offset((pageNumber - 1) * SHORT_PAGE_SIZE)
            .fetch();

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
            .where(team.privateStatus.eq(PUBLIC), team.id.in(teamIds))
            .orderBy(team.id.desc())
            .fetch();

        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
            .map(teamDto -> {
                List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                SimpleTeamReviewDto simpleTeamReview
                    = teamReviews.getOrDefault(
                    teamDto.teamId(),
                    new SimpleTeamReviewDto(teamDto.teamId(), 0D, 0L));
                return createSimpleDto(teamDto, tags, simpleTeamReview);
            })
            .sorted(comparing(SimpleTeamDto::teamId).reversed())
            .toList();
    }

    public Long searchTeamsCount(String searchText) {
        return teamJpaRepository.countByKeyword(searchText);
    }

    public List<SimpleTeamDto> searchTeams(String searchText, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }

        List<Long> teamIds = teamJpaRepository.searchTeamIdByKeyword(
            searchText,
            SHORT_PAGE_SIZE.longValue(),
            ((pageNumber.longValue()-1) * SHORT_PAGE_SIZE.longValue()));

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
                .leftJoin(teamDescription).on(team.id.eq(teamDescription.id))
                .where(team.id.in(teamIds))
                .fetch();

        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
                .map(teamDto -> {
                    List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                    List<String> resultTest = new ArrayList<>();
                    String area = null;
                    String category = null;
                    List<String> result = new ArrayList<>();
                    for (String tag : tags) {
                        if (Area.fromName(tag) != null) {
                            area = tag;
                            continue;
                        }
                        if (TeamCategory.fromName(tag) != null) {
                            category = tag;
                            continue;
                        }
                        resultTest.add(tag);
                    }
                    if (area != null) {
                        result.add(area);
                    }
                    if (category != null) {
                        result.add(category);
                    }
                    result.addAll(resultTest);
                    SimpleTeamReviewDto simpleTeamReview
                            = teamReviews.getOrDefault(
                            teamDto.teamId(),
                            new SimpleTeamReviewDto(teamDto.teamId(), 0D, 0L));
                    return createSimpleDto(teamDto, result, simpleTeamReview);
                })
                .sorted(comparing(SimpleTeamDto::teamId).reversed())
                .toList();
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
            .select(teamTag.teamId, teamTag.tagName)
            .from(teamTag)
            .where(teamTag.teamId.in(teamIds))
            .fetch();

        return results.stream()
            .collect(groupingBy(
                tuple -> tuple.get(teamTag.teamId),
                mapping(tuple -> tuple.get(teamTag.tagName), toList())
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
