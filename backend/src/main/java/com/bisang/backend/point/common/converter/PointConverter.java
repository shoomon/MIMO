package com.bisang.backend.point.common.converter;

import jakarta.persistence.Converter;

import com.bisang.backend.point.common.domain.PointTransaction;
import com.bisang.backend.point.common.domain.TransactionKind;
import com.bisang.backend.point.charge.controller.request.PaymentResultRequest;

@Converter
public class PointConverter {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    // 포인트 충전
    public static PointTransaction PaymentResultRequestToPointTransaction(PaymentResultRequest paymentResultRequest) {
        return new PointTransaction(
                paymentResultRequest.getPaidAmount(),
                ADMIN_ACCOUNT_NUMBER,
                paymentResultRequest.getReceiverAccountNumber(),
                "관리자",
                paymentResultRequest.getReceiverName(),
                paymentResultRequest.getImpUid(),
                paymentResultRequest.getMerchantUid(),
                "충전",
                TransactionKind.CHARGE
                );
    }
}

