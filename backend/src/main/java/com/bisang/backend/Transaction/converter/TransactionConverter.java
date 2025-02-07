package com.bisang.backend.Transaction.converter;

import com.bisang.backend.Transaction.controller.request.PaymentResultRequest;
import com.bisang.backend.Transaction.controller.request.TransferRequest;
import com.bisang.backend.Transaction.domain.Transaction;
import com.bisang.backend.Transaction.domain.TransactionCategory;
import com.bisang.backend.Transaction.domain.TransactionStatus;
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

    // 잔액 송금 트랜잭션
    public static Transaction TransferRequestToTransaction(TransferRequest transferRequest) {
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

