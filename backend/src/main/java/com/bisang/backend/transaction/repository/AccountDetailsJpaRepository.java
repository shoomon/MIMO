package com.bisang.backend.transaction.repository;

import com.bisang.backend.transaction.domain.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsJpaRepository extends JpaRepository<AccountDetails, Long> {
}
