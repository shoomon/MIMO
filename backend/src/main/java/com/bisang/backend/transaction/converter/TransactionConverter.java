package com.bisang.backend.transaction.converter;

import org.springframework.stereotype.Component;

import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.domain.TransactionStatus;
import com.bisang.backend.user.domain.User;

@Component
public class TransactionConverter {

    // 잔액 충전 트랜잭션
    public static Transaction chargeRequestToTransaction(ChargeRequest chargeRequest, User user) {
        return Transaction.builder()
                .amount(chargeRequest.getAmount())
                .senderAccountNumber(null)
                .receiverAccountNumber(user.getAccountNumber())
                .impUid(chargeRequest.getImpUid())
                .merchantUid(chargeRequest.getMerchantUid())
                .memo("충전")
                .transactionCategory(TransactionCategory.CHARGE)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }

    // 잔액 송금 트랜잭션
    public static Transaction transferRequestToTransaction(TransferRequest transferRequest, User user) {
        return Transaction.builder()
                .amount(transferRequest.getAmount())
                .senderAccountNumber(user.getAccountNumber())
                .receiverAccountNumber(transferRequest.getReceiverAccountNumber())
                .impUid(null)
                .merchantUid(null)
                .memo("송금")
                .transactionCategory(TransactionCategory.TRANSFER)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }

    // 잔액 결제 트랜잭션
    public static Transaction paymentRequestToTransaction(PaymentRequest paymentRequest, String senderAccountNumber) {
        return Transaction.builder()
                .amount(paymentRequest.getAmount())
                .senderAccountNumber(senderAccountNumber)
                .receiverAccountNumber(paymentRequest.getReceiverAccountNumber())
                .impUid(null)
                .merchantUid(null)
                .memo(paymentRequest.getMemo())
                .transactionCategory(TransactionCategory.PAYMENT)
                .transactionStatus(TransactionStatus.PENDING)
                .build();
    }
}
