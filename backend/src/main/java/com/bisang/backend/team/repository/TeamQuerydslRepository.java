package com.bisang.backend.team.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.QTag.tag;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static java.util.Comparator.comparing;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.JPAExpressions;
import org.springframework.stereotype.Repository;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamDto;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamQuerydslRepository {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final JPAQueryFactory queryFactory;

    public TeamDto getTeamInfo(Long teamId) {

        Long currentMemberCount = teamUserJpaRepository.countTeamUserByTeamId(teamId);

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
                        Expressions.numberTemplate(Long.class, "{0}", currentMemberCount)
                ))
                .from(team).join(teamDescription).on(team.description.id.eq(teamDescription.id))
                .where(team.id.eq(teamId))
                .fetchOne()).orElseThrow(() -> new TeamException(NOT_FOUND));
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
                    team.maxCapacity,
                    JPAExpressions.select(teamUser.count())
                            .from(teamUser)
                            .where(teamUser.teamId.eq(team.id)),
                    Expressions.constant(Collections.emptyList())
            ))
            .from(team)
            .where(team.areaCode.eq(areaCode).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        return teams.stream()
                .map(teamDto -> {
                    List<String> tags = getTags(teamDto.teamId());
                    return createSimpleDto(teamDto, tags);
                }).sorted(comparing(SimpleTeamDto::teamId)).toList();
    }

    public List<SimpleTeamDto> getTeamsByCategory(TeamCategory category, Long teamId) {
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
                    Expressions.constant(Collections.emptyList())
            ))
            .from(team)
            .where(team.category.eq(category).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        return teams.stream()
            .map(teamDto -> {
                List<String> tags = getTags(teamDto.teamId());
                return createSimpleDto(teamDto, tags);
            }).sorted(comparing(SimpleTeamDto::teamId)).toList();
    }

    private List<String> getTags(Long teamId) {
        return queryFactory
            .select(tag.name)
            .from(teamTag)
            .join(tag).on(teamTag.tagId.eq(tag.id))
            .where(teamTag.teamId.eq(teamId))
            .fetch();
    }

    private SimpleTeamDto createSimpleDto(SimpleTeamDto dto, List<String> tags) {
        return new SimpleTeamDto(
            dto.teamId(),
            dto.name(),
            dto.description(),
            dto.teamProfileUri(),
            dto.reviewScore(),
            dto.maxCapacity(),
            dto.currentCapacity(),
            tags
        );
    }
}
