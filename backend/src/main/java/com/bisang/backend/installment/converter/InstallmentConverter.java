package com.bisang.backend.installment.converter;

import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.installment.domain.Installment;
import com.bisang.backend.installment.domain.InstallmentStatus;

import java.util.List;
import java.util.stream.Collectors;

public class InstallmentConverter
{
    public static List<Installment> installmentRequestsToInstallments(
            List<InstallmentRequest> installmentRequests
    ) {
        return installmentRequests.stream()
                .map(it -> Installment.builder()
                        .teamId(it.getTeamId())
                        .userId(it.getUserId())
                        .round(it.getRound())
                        .amount(it.getAmount())
                        .installmentStatus(InstallmentStatus.NO)
                        .installmentDate(null)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
