package com.bisang.backend.transaction.converter;

import org.springframework.stereotype.Component;

import com.bisang.backend.transaction.controller.request.PaymentResultRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.domain.TransactionStatus;

@Component
public class TransactionConverter {

    // 잔액 충전 트랜잭션
    public static Transaction paymentResultRequestToTransaction(PaymentResultRequest paymentResultRequest) {
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

    // 잔액 송금 트랜잭션
    public static Transaction transferRequestToTransaction(TransferRequest transferRequest) {
        return Transaction.builder()
                .balance(transferRequest.getBalance())
                .senderAccountNumber(transferRequest.getSenderAccountNumber())
                .receiverAccountNumber(transferRequest.getReceiverAccountNumber())
                .senderName(transferRequest.getSenderName())
                .receiverName(transferRequest.getReceiverName())
                .impUid(null)
                .merchantUid(null)
                .memo("송금")
                .transactionCategory(TransactionCategory.TRANSFER)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }
}
