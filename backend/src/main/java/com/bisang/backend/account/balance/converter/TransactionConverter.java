package com.bisang.backend.account.balance.converter;

import com.bisang.backend.account.balance.domain.Transaction;
import com.bisang.backend.account.balance.controller.request.PaymentResultRequest;
import com.bisang.backend.account.balance.domain.TransactionCategory;
import com.bisang.backend.account.balance.domain.TransactionStatus;
import jakarta.persistence.Converter;

@Converter
public class TransactionConverter {

    // 잔액 충전 트랜잭션
    public static Transaction PaymentResultRequestToTransaction(PaymentResultRequest paymentResultRequest) {
        return Transaction.builder()
                .balance(paymentResultRequest.getPaidAmount())
                .senderAccountNumber(null)
                .receiverAccountNumber(paymentResultRequest.getReceiverAccountNumber())
                .senderName(null)
                .receiverName(paymentResultRequest.getReceiverName())
                .impUid(paymentResultRequest.getImpUid())
                .merchantUid(paymentResultRequest.getMerchantUid())
                .memo("충전")
                .transactionCategory(TransactionCategory.CHARGE)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }
}

