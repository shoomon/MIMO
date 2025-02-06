package com.bisang.backend.team.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.team.domain.QTag.tag;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;

import java.util.List;
import java.util.Optional;

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
    private final JPAQueryFactory queryFactory;

    public TeamDto getTeamInfo(Long teamId) {

        Long currentMemberCount = queryFactory.select(teamUser.count())
                                                .from(teamUser)
                                                .where(teamUser.id.eq(teamId)).fetchOne();

        String description = queryFactory.select(teamDescription.description)
                                            .from(teamDescription)
                                            .where(teamDescription.id.eq(teamId)).fetchOne();

        return Optional.ofNullable(
                queryFactory
                .select(Projections.fields(TeamDto.class,
                        team.id,
                        team.teamProfileUri,
                        team.name,
                        Expressions.constant(description),
                        team.recruitStatus,
                        team.privateStatus,
                        team.areaCode,
                        team.maxCapacity,
                        Expressions.constant(currentMemberCount)))
                .from(team)
                .where(team.id.eq(teamId))
                .fetchOne()).orElseThrow(() -> new TeamException(NOT_FOUND));
    }

    public List<SimpleTeamDto> getTeamsByAreaCode(Area areaCode, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        List<SimpleTeamDto> teams = queryFactory
            .select(Projections.fields(SimpleTeamDto.class,
                team.id,
                team.name,
                team.shortDescription,
                team.teamProfileUri,
                Expressions.constant(0D),
                Expressions.constant(null)))
            .from(team)
            .where(team.areaCode.eq(areaCode).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        return  teams.stream()
            .map(teamDto -> {
                List<String> tags = getTags(teamId);
                return createSimpleDto(teamDto, tags);
            }).toList();
    }

    public List<SimpleTeamDto> getTeamsByCategory(TeamCategory category, Long teamId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(team.id.lt(teamId));
        }

        List<SimpleTeamDto> teams = queryFactory
            .select(Projections.fields(SimpleTeamDto.class,
                team.id,
                team.name,
                team.shortDescription,
                team.teamProfileUri,
                Expressions.constant(0D),
                Expressions.constant(null)))
            .from(team)
            .where(team.category.eq(category).and(dynamicTeamIdLt))
            .orderBy(team.id.desc())
            .limit(SHORT_PAGE_SIZE + 1).fetch();

        return teams.stream()
            .map(teamDto -> {
                List<String> tags = getTags(teamDto.teamId());
                return createSimpleDto(teamDto, tags);
            }).toList();
    }

    private List<String> getTags(Long teamDto) {
        return queryFactory
            .select(tag.name)
            .from(teamTag)
            .join(team).on(teamTag.tagId.eq(team.id))
            .where(teamTag.teamId.eq(teamDto))
            .fetch();
    }

    private SimpleTeamDto createSimpleDto(SimpleTeamDto dto, List<String> tags) {
        return new SimpleTeamDto(
            dto.teamId(),
            dto.name(),
            dto.description(),
            dto.teamProfileUri(),
            dto.reviewScore(),
            tags
        );
    }
}
