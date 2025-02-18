package com.bisang.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.account.domain.AccountDetails;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
}