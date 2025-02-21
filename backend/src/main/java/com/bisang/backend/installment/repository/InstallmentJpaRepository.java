package com.bisang.backend.installment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.installment.domain.Installment;


public interface InstallmentJpaRepository extends JpaRepository<Installment, Long> {
    Optional<Installment> findByTeamIdAndUserIdAndRound(
            Long teamId,
            Long userId,
            Long round
    );
}
