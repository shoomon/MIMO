package com.bisang.backend.Transaction.repository;


import com.bisang.backend.Transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogJpaRepository extends JpaRepository<Transaction, Long> {
}
