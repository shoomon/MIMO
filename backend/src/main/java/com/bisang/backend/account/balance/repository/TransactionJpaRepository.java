package com.bisang.backend.account.balance.repository;

import com.bisang.backend.account.balance.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
