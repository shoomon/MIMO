package com.bisang.backend.team.repository;

import static com.bisang.backend.invite.domain.InviteStatus.REJECTED;
import static com.bisang.backend.invite.domain.InviteStatus.WAITING;
import static com.bisang.backend.invite.domain.QTeamInvite.teamInvite;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bisang.backend.team.controller.dto.TeamInviteDto;
import com.bisang.backend.team.controller.dto.TeamUserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamUserQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<TeamUserDto> getTeamUserInfo(Long teamId) {
        return queryFactory
                .select(Projections.fields(TeamUserDto.class,
                        teamUser.id,
                        teamUser.nickname,
                        teamUser.role))
                .from(teamUser)
                .where(teamUser.teamId.eq(teamId)).fetch();
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
}
