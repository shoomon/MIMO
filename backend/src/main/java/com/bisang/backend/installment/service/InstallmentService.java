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

import com.bisang.backend.transaction.repository.AccountQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bisang.backend.installment.domain.InstallmentStatus.NO;
import static com.bisang.backend.installment.domain.InstallmentStatus.YES;

@Service
@RequiredArgsConstructor
public class InstallmentService {
        private final InstallmentJpaRepository installmentJpaRepository;
        private final AccountQuerydslRepository accountQuerydslRepository;

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
            return accountQuerydslRepository.findInstallmentResponsesByTeamId(
                    teamId,
                    round,
                    YES
            );
        }

        @TeamMember
        public List<InstallmentResponse> getInstallmentNonPayerDetails(
                Long userId,
                Long teamId,
                Long round
        ) {
            return accountQuerydslRepository.findInstallmentResponsesByTeamId(
                    teamId,
                    round,
                    NO
            );
        }

        @TeamMember
        public Boolean isUserPayInstallment(
                Long userId,
                Long teamId,
                Long round
        ) {
            Optional<Installment> optionalInstallment
                    = installmentJpaRepository.findByTeamIdAndUserIdAndRound(
                            teamId,
                            userId,
                            round
            );

            // 해당 회차에 할당되지 않은 유저는 마치 납부를 한 것과 같음
            return optionalInstallment.map(Installment::isUserPayInstallment).orElse(true);
        }
}
