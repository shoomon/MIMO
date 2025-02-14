package com.bisang.backend.transaction.repository;

import java.util.List;

import com.bisang.backend.transaction.domain.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.account.domain.AccountDetails;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
    List<AccountDetails> findBySenderAccountNumberAndTransactionCategory(String accountNumber, TransactionCategory transactionCategory);
    List<AccountDetails> findByReceiverAccountNumberAndTransactionCategory(String accountNumber, TransactionCategory transactionCategory
    );
}