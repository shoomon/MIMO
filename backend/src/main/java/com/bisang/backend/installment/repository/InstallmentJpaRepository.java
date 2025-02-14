package com.bisang.backend.installment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.installment.controller.response.InstallmentResponse;
import com.bisang.backend.installment.domain.Installment;


public interface InstallmentJpaRepository extends JpaRepository<Installment, Long> {
    @Query("SELECT new com.bisang.backend.installment.controller.response.InstallmentResponse(" +
            "u.nickName, i.amount, i.installmentDate) " +
            "FROM Installment i " +
            "JOIN User u ON i.userId = u.id " +
            "WHERE i.teamId = :teamId " +
            "AND i.round = :round " +
            "AND i.installmentStatus = :installmentStatus")
    List<InstallmentResponse> findInstallmentResponsesByTeamIdAndRoundAndInstallmentStatus(
            Long teamId,
            Long round,
            String installmentStatus
    );

    Optional<Installment> findByTeamIdAndRoundAndInstallmentStatus(
            Long teamId,
            Long userId,
            Long round
    );
}
