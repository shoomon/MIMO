package com.bisang.backend.installment.service;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.installment.controller.response.InstallmentResponse;
import com.bisang.backend.installment.converter.InstallmentConverter;
import com.bisang.backend.installment.domain.Installment;
import com.bisang.backend.installment.domain.InstallmentStatus;
import com.bisang.backend.installment.repository.InstallmentJpaRepository;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.annotation.TeamMember;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstallmentService {
    private final InstallmentJpaRepository installmentJpaRepository;

    @TeamLeader
    public void createInstallment(
            Long userId,
            Long teamId,
            List<InstallmentRequest> installmentRequests
    ) {
        List<Installment> installments
                = InstallmentConverter.installmentRequestsToInstallments(installmentRequests);

        installmentJpaRepository.saveAll(installments);
    }

    @TeamMember
    public List<InstallmentResponse> getInstallmentPayerDetails(
            Long userId,
            Long teamId,
            Long round
    ) {
        return installmentJpaRepository.findInstallmentResponsesByTeamIdAndRoundAndInstallmentStatus(
                teamId,
                round,
                InstallmentStatus.YES.toString()
        );
    }

    @TeamMember
    public List<InstallmentResponse> getInstallmentNonPayerDetails(
            Long userId,
            Long teamId,
            Long round
    ) {
        return installmentJpaRepository.findInstallmentResponsesByTeamIdAndRoundAndInstallmentStatus(
                teamId,
                round,
                InstallmentStatus.NO.toString()
        );
    }

    @TeamMember
    public Boolean isUserPayInstallment(
            Long userId,
            Long teamId,
            Long round
    ) {
        Optional<Installment> optionalInstallment
                = installmentJpaRepository.findByTeamIdAndRoundAndInstallmentStatus(
                        teamId,
                        userId,
                        round
        );

        // 해당 회차에 할당되지 않은 유저는 마치 납부를 한 것과 같음
        return optionalInstallment.map(Installment::isUserPayInstallment).orElse(true);
    }
}
