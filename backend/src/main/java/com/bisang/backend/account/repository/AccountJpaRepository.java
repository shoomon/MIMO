package com.bisang.backend.account.repository;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.bisang.backend.account.domain.Account;

public interface AccountJpaRepository extends JpaRepository<Account, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Account findByAccountNumberWithLockingReads(String accountNumber);

    Account findByAccountNumber(String accountNumber);
}
