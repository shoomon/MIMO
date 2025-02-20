package com.bisang.backend.team.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bisang.backend.team.controller.dto.*;
import com.bisang.backend.team.domain.*;
import org.springframework.stereotype.Repository;

import com.bisang.backend.common.exception.TeamException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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

    public MyTeamSpecificDto getMyTeamSpecificInfo(Long teamId) {
        Team findTeam = isTeamExistValidation(teamId);
        SimpleTeamReviewDto simpleTeamReview = getSimpleTeamReview(teamId);
        Long currentMemberCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);
        List<String> tags = getTags(teamId);

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(MyTeamSpecificDto.class,
                                team.id,
                                team.teamProfileUri,
                                team.name,
                                teamDescription.description,
                                team.recruitStatus,
                                team.privateStatus,
                                Expressions.constant(findTeam.getAreaCode().getName()),
                                Expressions.constant(findTeam.getCategory().getName()),
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

    public TeamDto getTeamInfo(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        TeamDescription teamDescription = team.getDescription();
        SimpleTeamReviewDto simpleTeamReview = getSimpleTeamReview(teamId);
        Long currentMemberCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);

        List<String> resultTags = new ArrayList<>();
        resultTags.add(team.getAreaCode().getName());
        resultTags.add(team.getCategory().getName());
        List<String> tags = getTags(teamId);
        tags.remove(team.getAreaCode().getName());
        tags.remove(team.getCategory().getName());
        resultTags.addAll(tags);

        return TeamDto.builder()
            .teamId(teamId)
            .profileUri(team.getTeamProfileUri())
            .name(team.getName())
            .description(teamDescription.getDescription())
            .accountNumber(team.getAccountNumber())
            .recruitStatus(team.getRecruitStatus())
            .privateStatus(team.getPrivateStatus())
            .maxCapacity(team.getMaxCapacity())
            .currentCapacity(currentMemberCount)
            .reviewScore(simpleTeamReview == null ? 0D : simpleTeamReview.reviewScore())
            .reviewCount(simpleTeamReview == null ? 0L : simpleTeamReview.reviewCount())
            .tags(resultTags)
            .build();
    }

    public SimpleTeamDto getSimpleTeamInfo(Long userId, Long teamId) {
        Team team = findTeamById(teamId);
        TeamDescription teamDescription = team.getDescription();
        SimpleTeamReviewDto simpleTeamReview = getSimpleTeamReview(teamId);
        Long userCount = getUserCount(teamId);

        List<String> resultTags = new ArrayList<>();
        resultTags.add(team.getAreaCode().getName());
        resultTags.add(team.getCategory().getName());
        List<String> tags = getTags(teamId);
        tags.remove(team.getAreaCode().getName());
        tags.remove(team.getCategory().getName());
        resultTags.addAll(tags);

        return SimpleTeamDto.builder()
            .teamId(teamId)
            .teamProfileUri(team.getTeamProfileUri())
            .name(team.getName())
            .description(teamDescription.getDescription())
            .maxCapacity(team.getMaxCapacity())
            .currentCapacity(userCount)
            .reviewScore(simpleTeamReview == null ? 0D : simpleTeamReview.reviewScore())
            .reviewCount(simpleTeamReview == null ? 0L : simpleTeamReview.reviewCount())
            .tags(resultTags)
            .build();
    }

    public List<SimpleTeamDto> getTeamsByAreaCode(Area areaCode, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        List<SpecificTeamDto> teams = queryFactory
            .select(Projections.constructor(SpecificTeamDto.class,
                    team.id,
                    team.teamProfileUri,
                    team.name,
                    team.shortDescription,
                    team.accountNumber,
                    team.recruitStatus,
                    team.privateStatus,
                    team.areaCode,
                    team.category,
                    team.maxCapacity,
                    JPAExpressions.select(teamUser.count())
                            .from(teamUser)
                            .where(teamUser.teamId.eq(team.id)),
                    Expressions.numberTemplate(Double.class, "{0}", 0.0),
                    Expressions.numberTemplate(Long.class, "{0}", 0L),
                    Expressions.constant(emptyList())
            ))
            .from(team)
            .where(team.privateStatus.eq(PUBLIC), team.areaCode.eq(areaCode).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        List<Long> teamIds = teams.stream()
            .map(SpecificTeamDto::teamId)
            .toList();
        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
            .map(teamDto -> {
                List<String> resultTest = new ArrayList<>();
                resultTest.add(teamDto.area().getName());
                resultTest.add(teamDto.category().getName());
                List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                if (!tags.isEmpty()) {
                    tags.remove(teamDto.area().getName());
                    tags.remove(teamDto.category().getName());
                }
                resultTest.addAll(tags);

                SimpleTeamReviewDto simpleTeamReview
                    = teamReviews.getOrDefault(
                        teamDto.teamId(),
                        new SimpleTeamReviewDto(teamDto.teamId(), 0D, 0L));
                return createSimpleDto(teamDto, resultTest, simpleTeamReview);
            })
            .sorted(comparing(SimpleTeamDto::teamId).reversed())
            .toList();
    }

    public List<SimpleTeamDto> getTeamsByCategory(TeamCategory category, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        List<SpecificTeamDto> teams = queryFactory
            .select(Projections.constructor(SpecificTeamDto.class,
                team.id,
                team.teamProfileUri,
                team.name,
                team.shortDescription,
                team.accountNumber,
                team.recruitStatus,
                team.privateStatus,
                team.areaCode,
                team.category,
                team.maxCapacity,
                JPAExpressions.select(teamUser.count())
                    .from(teamUser)
                    .where(teamUser.teamId.eq(team.id)),
                Expressions.numberTemplate(Double.class, "{0}", 0.0),
                Expressions.numberTemplate(Long.class, "{0}", 0L),
                Expressions.constant(emptyList())
            ))
            .from(team)
            .where(team.privateStatus.eq(PUBLIC), team.category.eq(category).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        List<Long> teamIds = teams.stream()
                                    .map(SpecificTeamDto::teamId)
                                    .toList();
        Map<Long, List<String>> tagsMap = getTagsByTeamIds(teamIds);
        Map<Long, SimpleTeamReviewDto> teamReviews = getReviewsByTeamIds(teamIds);

        return teams.stream()
            .map(teamDto -> {
                List<String> resultTest = new ArrayList<>();
                resultTest.add(teamDto.area().getName());
                resultTest.add(teamDto.category().getName());
                List<String> tags = tagsMap.getOrDefault(teamDto.teamId(), emptyList());
                if (!tags.isEmpty()) {
                    tags.remove(teamDto.area().getName());
                    tags.remove(teamDto.category().getName());
                }
                resultTest.addAll(tags);
                SimpleTeamReviewDto simpleTeamReview
                    = teamReviews.getOrDefault(
                        teamDto.teamId(),
                        new SimpleTeamReviewDto(teamDto.teamId(), 0D,0L));
                return createSimpleDto(teamDto, resultTest, simpleTeamReview);
            })
            .sorted(comparing(SimpleTeamDto::teamId).reversed())
            .toList();
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId)
            .orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    private Team isTeamExistValidation(Long teamId) {
        return teamJpaRepository.findTeamById(teamId)
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
            .select(teamTag.tagName)
            .from(teamTag)
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
            .select(teamTag.teamId,
                teamTag.tagName)
            .from(teamTag)
            .where(teamTag.teamId.in(teamIds))
            .fetch();

        return results.stream()
            .collect(groupingBy(
                tuple -> tuple.get(teamTag.teamId),
                mapping(tuple -> tuple.get(teamTag.tagName), toList())
            ));
    }

    private SimpleTeamDto createSimpleDto(SpecificTeamDto dto, List<String> tags, SimpleTeamReviewDto simpleTeamReview) {
        return new SimpleTeamDto(
            dto.teamId(),
            dto.name(),
            dto.description(),
            dto.profileUri(),
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
