package com.bisang.backend.transaction.converter;

import org.springframework.stereotype.Component;

import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.domain.TransactionStatus;

@Component
public class TransactionConverter {

    // 잔액 충전 트랜잭션
    public static Transaction chargeRequestToTransaction(ChargeRequest chargeRequest) {
        return Transaction.builder()
                .balance(chargeRequest.getPaidAmount())
                .senderAccountNumber(null)
                .receiverAccountNumber(chargeRequest.getReceiverAccountNumber())
                .senderName(null)
                .receiverName(chargeRequest.getReceiverName())
                .impUid(chargeRequest.getImpUid())
                .merchantUid(chargeRequest.getMerchantUid())
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

    // 잔액 결제 트랜잭션
    public static Transaction paymentRequestToTransaction(PaymentRequest paymentRequest) {
        return Transaction.builder()
                .balance(paymentRequest.getPaidAmount())
                .senderAccountNumber(paymentRequest.getSenderAccountNumber())
                .receiverAccountNumber(null)
                .senderName(paymentRequest.getSenderName())
                .receiverName(paymentRequest.getReceiverName())
                .impUid(null)
                .merchantUid(null)
                .memo(paymentRequest.getMemo())
                .transactionCategory(TransactionCategory.PAYMENT)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }
}
