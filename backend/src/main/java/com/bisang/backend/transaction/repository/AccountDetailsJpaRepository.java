package com.bisang.backend.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.transaction.domain.AccountDetails;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
}
