package com.bisang.backend.transaction.repository;

import com.bisang.backend.installment.controller.response.InstallmentResponse;
import com.bisang.backend.installment.domain.InstallmentStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bisang.backend.installment.domain.QInstallment.installment;
import static com.bisang.backend.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class AccountQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<InstallmentResponse> findInstallmentResponsesByTeamId(Long teamId, Long round, InstallmentStatus installmentStatus) {
        List<InstallmentResponse> fetch = queryFactory
            .select(Projections.constructor(InstallmentResponse.class,
                user.nickname,
                installment.amount,
                installment.installmentDate
            ))
            .from(installment).join(user)
            .on(installment.userId.eq(user.id))
            .where(installment.teamId.eq(teamId), installment.round.eq(round), installment.installmentStatus.eq(installmentStatus))
            .fetch();
        return fetch;
    }
}
