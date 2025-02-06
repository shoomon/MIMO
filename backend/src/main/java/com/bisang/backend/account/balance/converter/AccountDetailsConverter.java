package com.bisang.backend.account.balance.converter;

import com.bisang.backend.account.balance.domain.AccountDetails;
import com.bisang.backend.account.balance.domain.Transaction;
import jakarta.persistence.Converter;

@Converter
public class AccountDetailsConverter {

    // 잔액 충전 내역
    public static AccountDetails transactionToAccountDetails(Transaction transaction) {
        return AccountDetails.builder()
                .transactionId(transaction)
                .senderAccountNumber(transaction.getSenderAccountNumber())
                .receiverAccountNumber(transaction.getReceiverAccountNumber())
                .senderName(transaction.getSenderName())
                .receiverName(transaction.getReceiverName())
                .memo(transaction.getMemo())
                .transactionCategory(transaction.getTransactionCategory())
                .build();
    }
}
