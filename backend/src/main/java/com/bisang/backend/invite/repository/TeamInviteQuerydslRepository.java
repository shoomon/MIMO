package com.bisang.backend.invite.repository;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static com.bisang.backend.invite.domain.InviteStatus.REJECTED;
import static com.bisang.backend.invite.domain.InviteStatus.WAITING;
import static com.bisang.backend.invite.domain.QTeamInvite.teamInvite;
import static com.bisang.backend.user.domain.QUser.user;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bisang.backend.invite.controller.dto.TeamInviteDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamInviteQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public Long countTeamInvite(Long teamId) {
        return queryFactory
                .select(teamInvite.count())
                .from(teamInvite)
                .where(teamInvite.teamId.eq(teamId), teamInvite.status.in(REJECTED, WAITING))
                .fetchOne();
    }

    public List<TeamInviteDto> findTeamInvites(
        Long teamId, Long lastTeamInviteId
    ) {
        BooleanBuilder teamInviteIdLt = new BooleanBuilder();
        if (lastTeamInviteId != null) {
            teamInviteIdLt.and(teamInvite.id.lt(lastTeamInviteId));
        }

        List<TeamInviteDto> invites = new ArrayList<>();

        List<TeamInviteDto> waitingInvite = queryFactory
            .select(Projections.constructor(TeamInviteDto.class,
                teamInvite.id,
                teamInvite.teamId,
                teamInvite.userId,
                user.profileUri,
                teamInvite.status,
                user.name,
                teamInvite.memo))
            .from(teamInvite).join(user).on(teamInvite.userId.eq(user.id))
            .where(teamInvite.teamId.eq(teamId), teamInvite.status.eq(WAITING), teamInviteIdLt)
            .orderBy(teamInvite.id.desc())
            .limit(SHORT_PAGE_SIZE + 1)
            .fetch();
        invites.addAll(waitingInvite);

        List<TeamInviteDto> rejectedInvite = queryFactory
            .select(Projections.constructor(TeamInviteDto.class,
                teamInvite.id,
                teamInvite.teamId,
                teamInvite.userId,
                user.profileUri,
                teamInvite.status,
                user.name,
                teamInvite.memo))
            .from(teamInvite).join(user).on(teamInvite.userId.eq(user.id))
            .where(teamInvite.teamId.eq(teamId), teamInvite.status.eq(REJECTED), teamInviteIdLt)
            .orderBy(teamInvite.id.desc())
            .limit(SHORT_PAGE_SIZE + 1)
            .fetch();
        invites.addAll(rejectedInvite);

        return invites.stream()
            .sorted(comparing(TeamInviteDto::teamInviteId).reversed())
            .limit(SHORT_PAGE_SIZE + 1)
            .toList();
    }
}
