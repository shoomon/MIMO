package com.bisang.backend.team.repository;

import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.invite.domain.InviteStatus.REJECTED;
import static com.bisang.backend.invite.domain.InviteStatus.WAITING;
import static com.bisang.backend.invite.domain.QTeamInvite.teamInvite;
import static com.bisang.backend.team.domain.QTag.tag;
import static com.bisang.backend.team.domain.QTeam.team;
import static com.bisang.backend.team.domain.QTeamTag.teamTag;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.team.domain.TeamUserRole.*;
import static com.bisang.backend.user.domain.QUser.user;
import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bisang.backend.team.controller.dto.SimpleTeamDto;
import com.bisang.backend.team.controller.dto.TeamInviteDto;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.bisang.backend.team.domain.TeamUserRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamUserQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<SimpleTeamDto> getTeamsByTeamIdAndUserId(Long teamId, Long userId) {
        BooleanBuilder dynamicTeamIdLt = new BooleanBuilder();
        if (teamId != null) {
            dynamicTeamIdLt.and(teamUser.teamId.lt(teamId));
        }

        List<Long> teamKeys = queryFactory
                .select(teamUser.teamId)
                .from(teamUser)
                .where(teamUser.userId.eq(userId), dynamicTeamIdLt)
                .orderBy(teamUser.teamId.desc())
                .limit(SHORT_PAGE_SIZE + 1)
                .fetch();

        List<SimpleTeamDto> teams = queryFactory
                .select(Projections.constructor(SimpleTeamDto.class,
                        team.id,
                        Expressions.numberTemplate(Long.class, "{0}", 0L),
                        Expressions.nullExpression(TeamUserRole.class),
                        team.name,
                        team.shortDescription,
                        team.teamProfileUri,
                        Expressions.numberTemplate(Double.class, "{0}", 0.0),
                        Expressions.constant(0L),
                        team.maxCapacity,
                        JPAExpressions.select(teamUser.count())
                                .from(teamUser)
                                .where(teamUser.teamId.eq(team.id)),
                        Expressions.constant(Collections.emptyList())
                ))
                .from(team)
                .where(team.id.in(teamKeys))
                .orderBy(team.id.desc())
                .fetch();

        return teams.stream()
                .map(teamDto -> {
                    List<String> tags = getTags(teamDto.teamId());
                    return createSimpleDto(teamDto, tags);
                }).sorted(comparing(SimpleTeamDto::teamId).reversed()).toList();
    }

    public List<TeamInviteDto> getTeamInviteInfo(Long teamId) {

        return queryFactory
                .select(Projections.fields(TeamInviteDto.class,
                        teamInvite.id,
                        user.name,
                        teamInvite.memo,
                        teamInvite.status))
                .from(teamInvite)
                .join(user)
                .on(user.id.eq(teamInvite.userId))
                .where(teamInvite.teamId.eq(teamId)
                        .and(teamInvite.status.in(WAITING, REJECTED))).fetch();
    }

    public List<TeamUserDto> getTeamUserInfos(Long teamId, TeamUserRole role, Long teamUserId) {
        BooleanBuilder dynamicRoleEq = new BooleanBuilder();
        if (role != null) {
            dynamicRoleEq.and(teamUser.role.eq(role));
        }

        BooleanBuilder dynamicTeamUserIdGt = new BooleanBuilder();
        if (teamUserId != null) {
            dynamicRoleEq.and(teamUser.id.gt(teamUserId));
        }

        if (role == null && teamUserId == null) {
            List<TeamUserDto> leader = getLeaders(teamId);
            List<TeamUserDto> coLeader = getCoLeaders(teamId);

            if (leader.size() + coLeader.size() > PAGE_SIZE) {
                var teamUserDtos = mergeTeamUserDto(leader, coLeader);
                return sortTeamUserDto(teamUserDtos);
            }

            List<TeamUserDto> members = getMembers(teamId);
            var teamUserDtos = mergeTeamUserDto(leader, coLeader, members);
            return sortTeamUserDto(teamUserDtos);
        }

        List<TeamUserDto> result = getConditionResult(teamId, dynamicRoleEq, dynamicTeamUserIdGt);
        if (role == CO_LEADER) {
            if (result.size() > PAGE_SIZE) {
                return sortTeamUserDto(result);
            }
            List<TeamUserDto> members = getMembers(teamId);
            return sortTeamUserDto(mergeTeamUserDto(members, result));
        }
        return sortTeamUserDto(result);
    }

    private List<TeamUserDto> getConditionResult(
        Long teamId,
        BooleanBuilder dynamicRoleEq,
        BooleanBuilder dynamicTeamUserIdGt
    ) {
        return queryFactory
                .select(Projections.constructor(TeamUserDto.class,
                    user.id,
                    teamUser.id,
                    teamUser.nickname,
                    user.profileUri,
                    teamUser.role,
                    teamUser.createdAt))
                .from(teamUser).join(user).on(teamUser.userId.eq(user.id))
                .where(teamUser.teamId.eq(teamId), dynamicRoleEq, dynamicTeamUserIdGt).fetch();
    }

    private List<TeamUserDto> getLeaders(Long teamId) {
        return queryFactory
                .select(Projections.constructor(TeamUserDto.class,
                        user.id,
                        teamUser.id,
                        teamUser.nickname,
                        user.profileUri,
                        teamUser.role,
                        teamUser.createdAt))
                .from(teamUser).join(user).on(teamUser.userId.eq(user.id))
                .where(teamUser.teamId.eq(teamId), teamUser.role.eq(LEADER)).fetch();
    }

    private List<TeamUserDto> getCoLeaders(Long teamId) {
        return queryFactory
                .select(Projections.constructor(TeamUserDto.class,
                        user.id,
                        teamUser.id,
                        teamUser.nickname,
                        user.profileUri,
                        teamUser.role,
                        teamUser.createdAt))
                .from(teamUser).join(user).on(teamUser.userId.eq(user.id))
                .where(teamUser.teamId.eq(teamId), teamUser.role.eq(CO_LEADER))
                .orderBy(teamUser.id.asc()).fetch();
    }

    private List<TeamUserDto> getMembers(Long teamId) {
        return queryFactory
                .select(Projections.constructor(TeamUserDto.class,
                        user.id,
                        teamUser.id,
                        teamUser.nickname,
                        user.profileUri,
                        teamUser.role,
                        teamUser.createdAt))
                .from(teamUser).join(user).on(teamUser.userId.eq(user.id))
                .where(teamUser.teamId.eq(teamId), teamUser.role.eq(MEMBER))
                .orderBy(teamUser.id.asc()).fetch();
    }

    private List<TeamUserDto> sortTeamUserDto(List<TeamUserDto> teamUserDtos) {
        return teamUserDtos.stream()
            .sorted(comparing((TeamUserDto dto) ->dto.role().getWeight())
                .thenComparing(TeamUserDto::teamUserId))
            .limit(PAGE_SIZE + 1)
            .toList();
    }

    private List<TeamUserDto> mergeTeamUserDto(List<TeamUserDto>... teamUserDtoLists) {
        return Arrays.stream(teamUserDtoLists)
            .flatMap(List::stream)
            .toList();
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
                dto.teamUserId(),
                dto.role(),
                dto.name(),
                dto.description(),
                dto.teamProfileUri(),
                dto.reviewScore(),
                0L,
                dto.maxCapacity(),
                dto.currentCapacity(),
                tags
        );
    }
}
