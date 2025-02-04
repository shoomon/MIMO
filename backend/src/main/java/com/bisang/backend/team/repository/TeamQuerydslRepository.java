package com.bisang.backend.team.repository;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamDescription.teamDescription;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.controller.dto.TeamDto;
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
}
