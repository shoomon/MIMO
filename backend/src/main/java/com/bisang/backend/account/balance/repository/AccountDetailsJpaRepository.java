package com.bisang.backend.account.balance.repository;

import com.bisang.backend.account.balance.domain.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
}
