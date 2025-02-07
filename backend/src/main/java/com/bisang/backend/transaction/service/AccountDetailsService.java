package com.bisang.backend.transaction.service;

import com.bisang.backend.transaction.domain.AccountDetails;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.repository.AccountDetailsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {

    private AccountDetailsJpaRepository accountDetailsJpaRepository;

    public AccountDetails createAccountDetails(
            Transaction transaction,
            TransactionCategory transactionCategory,
            String memo
    ) {
        return AccountDetails.builder()
                .transactionId(transaction)
                .balance(transaction.getBalance())
                .senderAccountNumber(transaction.getSenderAccountNumber())
                .receiverAccountNumber(transaction.getReceiverAccountNumber())
                .senderName(transaction.getSenderName())
                .receiverName(transaction.getReceiverName())
                .memo(memo)
                .transactionCategory(transactionCategory)
                .build();
    }

    public void saveAccountDetails(AccountDetails accountDetails) {
        accountDetailsJpaRepository.save(accountDetails);
    }
}
