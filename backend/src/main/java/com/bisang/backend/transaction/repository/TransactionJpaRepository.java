package com.bisang.backend.transaction.repository;

import com.bisang.backend.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
