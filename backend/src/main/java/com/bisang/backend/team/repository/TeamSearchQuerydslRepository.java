package com.bisang.backend.team.repository;

import com.bisang.backend.team.controller.dto.*;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.domain.TeamTag;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.QTag.tag;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamReview.teamReview;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PRIVATE;
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

    public List<SimpleTagDto> findAreaTags() {
        List<String> areaNames = Area.getAreaNames();
        return queryFactory
            .select(Projections.constructor(SimpleTagDto.class,
                tag.id,
                tag.name
            ))
            .from(tag)
            .where(tag.name.in(areaNames))
            .fetch();
    }

    public List<SimpleTagDto> findCategoryTags() {
        List<String> categoryNames = TeamCategory.getCategoryNames();
        return queryFactory
            .select(Projections.constructor(SimpleTagDto.class,
                tag.id,
                tag.name
            ))
            .from(tag)
            .where(tag.name.in(categoryNames))
            .fetch();
    }

    public Optional<TeamTag> findByName(Long teamId, String name) {
        return Optional.ofNullable(queryFactory
                .select(teamTag)
                .from(teamTag).join(tag).on(teamTag.tagId.eq(tag.id))
                .where(tag.name.eq(name), teamTag.teamId.eq(teamId)).fetchOne());
    }

    public List<SimpleTagDto> searchTags(String searchText, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }

        return queryFactory
            .select(Projections.constructor(SimpleTagDto.class,
                tag.id,
                tag.name))
            .from(tag)
            .where(tag.name.contains(searchText))
            .orderBy(tag.name.desc())
            .limit(SHORT_PAGE_SIZE)
            .offset((pageNumber - 1) * SHORT_PAGE_SIZE)
            .fetch();
    }

    public Long searchTagsCount(String searchText) {
        return queryFactory
            .select(tag.count())
            .from(tag)
            .where(tag.name.contains(searchText))
            .fetchOne();
    }

    public Long searchTeamsCount(Long tagId) {
        return queryFactory
            .select(teamTag.teamId.count())
            .from(teamTag).join(team).on(teamTag.teamId.eq(team.id))
            .where(team.privateStatus.eq(PUBLIC), teamTag.tagId.eq(tagId))
            .fetchOne();
    }

    public List<SimpleTeamDto> searchTeams(Long tagId, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }

        List<Long> teamIds = queryFactory
            .select(teamTag.teamId)
            .from(teamTag)
            .where(teamTag.tagId.eq(tagId))
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
        return queryFactory
                .select(team.count())
                .from(team)
                .leftJoin(teamDescription).on(team.id.eq(teamDescription.id))
                .where(team.privateStatus.eq(PUBLIC), team.name.contains(searchText)
                        .or(teamDescription.description.contains(searchText)))
                .fetchOne();
    }

    public List<SimpleTeamDto> searchTeams(String searchText, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
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
                .leftJoin(teamDescription).on(team.id.eq(teamDescription.id))
                .where(team.privateStatus.eq(PUBLIC), team.name.contains(searchText)
                        .or(teamDescription.description.contains(searchText)))
                .limit(SHORT_PAGE_SIZE)
                .offset((pageNumber - 1) * SHORT_PAGE_SIZE)
                .fetch();

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
                            new SimpleTeamReviewDto(teamDto.teamId(), 0D, 0L));
                    return createSimpleDto(teamDto, tags, simpleTeamReview);
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
