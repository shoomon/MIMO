package com.bisang.backend.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.account.domain.AccountDetails;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
    List<AccountDetails> findBySenderAccountNumber(String accountNumber, String status);
    List<AccountDetails> findByReceiverAccountNumber(String accountNumber, String status);
}