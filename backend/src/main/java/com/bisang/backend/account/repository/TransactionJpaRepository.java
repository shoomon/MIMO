package com.bisang.backend.account.repository;

import com.bisang.backend.account.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
