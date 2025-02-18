package com.bisang.backend.installment.repository;

import com.bisang.backend.installment.controller.response.InstallmentResponse;
import com.bisang.backend.installment.domain.InstallmentStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bisang.backend.installment.domain.QInstallment.installment;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;

@Repository
@RequiredArgsConstructor
public class InstallmentQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<InstallmentResponse> findInstallmentResponsesByTeamId(Long teamId, Long round, InstallmentStatus installmentStatus) {
        List<InstallmentResponse> fetch = queryFactory
            .select(Projections.constructor(InstallmentResponse.class,
                teamUser.nickname,
                installment.amount,
                installment.installmentDate
            ))
            .from(installment).join(teamUser)
            .on(installment.userId.eq(teamUser.userId))
            .where(installment.teamId.eq(teamId), installment.round.eq(round), installment.installmentStatus.eq(installmentStatus))
            .fetch();
        return fetch;
    }
}
